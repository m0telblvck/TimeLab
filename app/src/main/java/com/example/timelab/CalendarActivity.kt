    package com.example.timelab

    import android.app.AlarmManager
    import android.app.PendingIntent
    import android.content.Context
    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.CalendarView
    import android.widget.TimePicker
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken
    import java.util.*

    class CalendarActivity : AppCompatActivity() {

        private lateinit var calendarView: CalendarView
        private lateinit var taskRecyclerView: RecyclerView
        private lateinit var taskAdapter: TaskAdapter

        private val taskManager = TaskManager()
        private var selectedDateString: String = ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_calendar)




            setupBottomNavigation(this, R.id.menu_item_2)

            calendarView = findViewById(R.id.calendarView)
            taskRecyclerView = findViewById(R.id.taskRecyclerView)






            // Настройка RecyclerView
            taskAdapter = TaskAdapter(mutableListOf()) { selectedTask: Task ->
                val editDialog = EditTaskBottomSheetFragment(
                    task = selectedTask,
                    onTaskUpdated = { updatedTask ->
                        taskManager.updateTask(selectedDateString, selectedTask, updatedTask)
                        taskManager.saveToPrefs(this)
                        val calendar = taskManager.parseDate(selectedDateString)
                        updateTasksForDate(calendar)
                    },
                    onTaskDeleted = { deletedTask ->
                        taskManager.removeTask(selectedDateString, deletedTask)
                        taskManager.saveToPrefs(this)
                        val calendar = taskManager.parseDate(selectedDateString)
                        updateTasksForDate(calendar)
                        Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show()
                    }
                )
                editDialog.show(supportFragmentManager, "EditTask")
            }


            taskRecyclerView.layoutManager = LinearLayoutManager(this)
            taskRecyclerView.adapter = taskAdapter

            // Восстановление задач из SharedPreferences
            taskManager.loadFromPrefs(this)

            // Установка текущей даты
            val calendar = Calendar.getInstance()
            selectedDateString = taskManager.formatDate(calendar)
            updateTasksForDate(calendar)

            // Обработка выбора даты
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDateString = taskManager.formatDate(calendar)
                updateTasksForDate(calendar)
            }

            // Кнопка добавления задачи
            findViewById<Button>(R.id.addTaskButton).setOnClickListener {
                val newTask = Task(
                    title = "Новая задача",
                    description = "Описание",
                    dueDate = selectedDateString
                )
                taskManager.addTaskForDate(selectedDateString, newTask)
                taskManager.saveToPrefs(this)
                val calendar = taskManager.parseDate(selectedDateString)
                updateTasksForDate(calendar)
                Toast.makeText(this, "Задача добавлена", Toast.LENGTH_SHORT).show()
            }
        }


        private fun updateTasksForDate(date: Calendar) {
            val tasks = taskManager.getTasksForDate(taskManager.formatDate(date))
            taskAdapter.submitList(tasks.toMutableList())
        }

        fun setTaskReminder(task: Task, hour: Int, minute: Int) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, NotificationReceiver::class.java).apply {
                putExtra("TASK_TITLE", task.title)
                putExtra("TASK_DESCRIPTION", task.description)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this, task.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Toast.makeText(this, "Напоминание установлено на $hour:$minute", Toast.LENGTH_SHORT).show()
        }

    }
