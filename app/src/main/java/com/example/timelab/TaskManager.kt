package com.example.timelab

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class TaskManager {
    private val tasksByDate = mutableMapOf<String, MutableList<Task>>()
    private val tasksByCategory = mutableMapOf<String, MutableList<Task>>()
    fun addTaskForDate(dateString: String, task: Task) {
        val taskList = tasksByDate.getOrPut(dateString) { mutableListOf() }
        taskList.add(task)
    }

    fun getTasksForDate(dateString: String): List<Task> {
        return tasksByDate[dateString] ?: emptyList()
    }

    fun formatDate(date: Calendar): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(date.time)
    }


    fun saveToPrefs(context: Context) {
        val shared = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val json = Gson().toJson(tasksByDate)
        shared.edit().putString("tasks_map", json).apply()
    }



    fun loadFromPrefs(context: Context) {
        val shared = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val json = shared.getString("tasks_map", null)
        if (json != null) {
            val type = object : TypeToken<MutableMap<String, MutableList<Task>>>() {}.type
            val restored = Gson().fromJson<MutableMap<String, MutableList<Task>>>(json, type)
            tasksByDate.clear()
            tasksByDate.putAll(restored)
        }
    }
    fun updateTask(dateKey: String, oldTask: Task, newTask: Task) {
        val tasks = tasksByDate[dateKey] ?: return
        val index = tasks.indexOfFirst { it == oldTask }
        if (index != -1) {
            tasks[index] = newTask
        }
    }
    fun parseDate(dateString: String): Calendar {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = sdf.parse(dateString) ?: Date()
        return Calendar.getInstance().apply { time = date }
    }


    fun removeTask(date: String, task: Task) {
        tasksByDate[date]?.remove(task)
    }

    fun addTaskForCategory(category: String, task: Task) {
        val taskList = tasksByCategory.getOrPut(category) { mutableListOf() }
        taskList.add(task)
    }

    // Получение задач для конкретной категории
    fun getTasksForCategory(category: String): List<Task> {
        return tasksByCategory[category] ?: emptyList()
    }
}
