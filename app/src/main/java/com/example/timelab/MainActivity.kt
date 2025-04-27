package com.example.timelab

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var noTasksImageView: ImageView
    private lateinit var noTasksTextView: TextView
    private lateinit var addTaskTextView: TextView
    private lateinit var moreOptionsButton: ImageView


    private val taskAdapter = TaskAdapter(mutableListOf()) { task ->
        openTaskBottomSheet(task)
    }
    private var allTasks: MutableList<Task> = mutableListOf() // Храним все задачи
    private var hideCompleted = false // Для фильтрации выполненных
    private val prefs by lazy { getSharedPreferences("tasks_prefs", MODE_PRIVATE) }
    private val gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupBottomNavigation(this, R.id.menu_item_1)

        moreOptionsButton = findViewById(R.id.moreOptionsButton)
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        addButton = findViewById(R.id.addButton)
        noTasksImageView = findViewById(R.id.noTasksImageView)
        noTasksTextView = findViewById(R.id.noTasksTextView)
        addTaskTextView = findViewById(R.id.addTaskTextView)

        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.adapter = taskAdapter

        loadTasks()


        addButton.setOnClickListener {
            val currentDate = java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(java.util.Date())
            val newTask = Task(
                title = "Новая задача",
                description = "Описание новой задачи",
                dueDate = currentDate
            )
            addTask(newTask)
            saveTasks()
        }


        updateEmptyState()
        moreOptionsButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.top_bar_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_clear_all -> {
                        clearAllTasks()
                        true
                    }
                    R.id.menu_sort -> {
                        sortTasksByDate()
                        true
                    }
                    R.id.menu_hide_completed -> {
                        toggleHideCompleted()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }



    private fun addTask(task: Task) {
        allTasks.add(task)
        taskAdapter.submitList(allTasks)
        updateEmptyState()
    }


    private fun updateEmptyState() {
        val isEmpty = taskAdapter.itemCount == 0
        noTasksImageView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        noTasksTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        addTaskTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun openTaskBottomSheet(task: Task) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_task, null)

        val editTitle = view.findViewById<EditText>(R.id.editTitle)
        val editDescription = view.findViewById<EditText>(R.id.editDescription)
        val completedCheckbox = view.findViewById<CheckBox>(R.id.taskCompletedCheckbox)
        val selectDateButton = view.findViewById<Button>(R.id.selectDateButton)
        val saveButton = view.findViewById<Button>(R.id.saveButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        editTitle.setText(task.title)
        editDescription.setText(task.description)
        completedCheckbox.isChecked = task.isCompleted
        selectDateButton.text = task.dueDate ?: "Выбрать дату"

        selectDateButton.setOnClickListener {
            val dialogDate = DatePickerDialog(this)
            dialogDate.setOnDateSetListener { _, year, month, day ->
                val selectedDate = String.format("%02d.%02d.%d", day, month + 1, year)
                task.dueDate = selectedDate
                selectDateButton.text = selectedDate
            }
            dialogDate.show()
        }

        saveButton.setOnClickListener {
            task.title = editTitle.text.toString()
            task.description = editDescription.text.toString()
            task.isCompleted = completedCheckbox.isChecked
            taskAdapter.notifyDataSetChanged()
            dialog.dismiss()
            Toast.makeText(this, "Задача обновлена", Toast.LENGTH_SHORT).show()
        }

        deleteButton.setOnClickListener {
            val position = taskAdapter.tasks.indexOf(task)
            if (position != -1) {
                taskAdapter.tasks.removeAt(position)
                taskAdapter.notifyItemRemoved(position)
            }
            dialog.dismiss()
            updateEmptyState()
            Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show()
        }

        dialog.setContentView(view)
        dialog.show()

    }

    private fun clearAllTasks() {
        allTasks.clear()
        taskAdapter.submitList(allTasks)
        updateEmptyState()
        Toast.makeText(this, "Все задачи удалены", Toast.LENGTH_SHORT).show()
    }

    private fun sortTasksByDate() {
        allTasks = allTasks.sortedBy { it.dueDate }.toMutableList() // Сортируем и обновляем список
        taskAdapter.submitList(allTasks)
        Toast.makeText(this, "Сортировка по дате", Toast.LENGTH_SHORT).show()
    }

    private fun toggleHideCompleted() {
        hideCompleted = !hideCompleted
        val filteredList = if (hideCompleted) {
            allTasks.filter { !it.isCompleted }.toMutableList()
        } else {
            allTasks // Возвращаем все задачи
        }
        taskAdapter.submitList(filteredList) // Обновляем список в адаптере
        val msg = if (hideCompleted) "Скрыты выполненные" else "Показаны все"
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun saveTasks() {
        val json = gson.toJson(allTasks)
        prefs.edit().putString("task_list", json).apply()
    }

    private fun loadTasks() {
        val json = prefs.getString("task_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            allTasks = gson.fromJson(json, type)
            taskAdapter.submitList(allTasks)
        }
    }
}