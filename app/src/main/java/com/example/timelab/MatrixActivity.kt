package com.example.timelab

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MatrixActivity : AppCompatActivity() {

    private lateinit var recyclerUrgentImportant: RecyclerView
    private lateinit var recyclerNotUrgentImportant: RecyclerView
    private lateinit var recyclerUrgentNotImportant: RecyclerView
    private lateinit var recyclerNotUrgentNotImportant: RecyclerView

    private lateinit var taskAdapterUrgentImportant: TaskAdapter
    private lateinit var taskAdapterNotUrgentImportant: TaskAdapter
    private lateinit var taskAdapterUrgentNotImportant: TaskAdapter
    private lateinit var taskAdapterNotUrgentNotImportant: TaskAdapter

    private val taskManager = TaskManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix)

        setupBottomNavigation(this, R.id.menu_item_3)

        // Инициализация RecyclerView
        recyclerUrgentImportant = findViewById(R.id.recyclerUrgentImportant)
        recyclerNotUrgentImportant = findViewById(R.id.recyclerNotUrgentImportant)
        recyclerUrgentNotImportant = findViewById(R.id.recyclerUrgentNotImportant)
        recyclerNotUrgentNotImportant = findViewById(R.id.recyclerNotUrgentNotImportant)

        // Настройка адаптеров для каждого RecyclerView
        taskAdapterUrgentImportant = TaskAdapter(mutableListOf()) { task ->
            handleTaskClick(task, "Срочно и важно")
        }
        taskAdapterNotUrgentImportant = TaskAdapter(mutableListOf()) { task ->
            handleTaskClick(task, "Не срочно, но важно")
        }
        taskAdapterUrgentNotImportant = TaskAdapter(mutableListOf()) { task ->
            handleTaskClick(task, "Срочно, но не важно")
        }
        taskAdapterNotUrgentNotImportant = TaskAdapter(mutableListOf()) { task ->
            handleTaskClick(task, "Не срочно и не важно")
        }

        // Установка LayoutManager
        recyclerUrgentImportant.layoutManager = LinearLayoutManager(this)
        recyclerNotUrgentImportant.layoutManager = LinearLayoutManager(this)
        recyclerUrgentNotImportant.layoutManager = LinearLayoutManager(this)
        recyclerNotUrgentNotImportant.layoutManager = LinearLayoutManager(this)

        recyclerUrgentImportant.adapter = taskAdapterUrgentImportant
        recyclerNotUrgentImportant.adapter = taskAdapterNotUrgentImportant
        recyclerUrgentNotImportant.adapter = taskAdapterUrgentNotImportant
        recyclerNotUrgentNotImportant.adapter = taskAdapterNotUrgentNotImportant

        // Загрузка задач
        loadTasks()

        // Кнопка добавления новой задачи
        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            showCategorySelectionDialog()
        }
    }

    // Метод для загрузки задач для всех категорий
    private fun loadTasks() {
        taskManager.loadFromPrefs(this)

        val urgentImportantTasks = taskManager.getTasksForCategory("Срочно и важно")
        val notUrgentImportantTasks = taskManager.getTasksForCategory("Не срочно, но важно")
        val urgentNotImportantTasks = taskManager.getTasksForCategory("Срочно, но не важно")
        val notUrgentNotImportantTasks = taskManager.getTasksForCategory("Не срочно и не важно")

        taskAdapterUrgentImportant.submitList(urgentImportantTasks)
        taskAdapterNotUrgentImportant.submitList(notUrgentImportantTasks)
        taskAdapterUrgentNotImportant.submitList(urgentNotImportantTasks)
        taskAdapterNotUrgentNotImportant.submitList(notUrgentNotImportantTasks)
    }

    // Метод для добавления новой задачи
    private fun addNewTask(category: String) {
        val newTask = Task(
            title = "Новая задача",
            description = "Описание задачи",
            category = category
        )
        taskManager.addTaskForCategory(category, newTask)
        taskManager.saveToPrefs(this)
        loadTasks()
        Toast.makeText(this, "Задача добавлена", Toast.LENGTH_SHORT).show()
    }

    private fun handleTaskClick(task: Task, category: String) {
        val editDialog = EditTaskBottomSheetFragment(
            task = task,
            onTaskUpdated = { updatedTask ->
                taskManager.updateTask(category, task, updatedTask)
                taskManager.saveToPrefs(this)
                loadTasks()
            },
            onTaskDeleted = { deletedTask ->
                taskManager.removeTask(category, deletedTask)
                taskManager.saveToPrefs(this)
                loadTasks()
                Toast.makeText(this, "Задача удалена", Toast.LENGTH_SHORT).show()
            }
        )
        editDialog.show(supportFragmentManager, "EditTask")
    }


    // Метод для отображения кастомного диалога с выбором категории
    private fun showCategorySelectionDialog() {
        val categories = arrayOf("Срочно и важно", "Не срочно, но важно", "Срочно, но не важно", "Не срочно и не важно")

        val dialogView = layoutInflater.inflate(R.layout.dialog_category_selection, null)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.categoryList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CategoryAdapter(categories) { selectedCategory ->
            addNewTask(selectedCategory)
        }

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)

        builder.show()
    }
}
