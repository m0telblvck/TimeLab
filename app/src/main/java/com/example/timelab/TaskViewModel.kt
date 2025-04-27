package com.example.timelab

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    private val _urgentImportantTasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    private val _notUrgentImportantTasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    private val _urgentNotImportantTasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    private val _notUrgentNotImportantTasks = MutableLiveData<MutableList<Task>>(mutableListOf())

    val urgentImportantTasks: LiveData<MutableList<Task>> get() = _urgentImportantTasks
    val notUrgentImportantTasks: LiveData<MutableList<Task>> get() = _notUrgentImportantTasks
    val urgentNotImportantTasks: LiveData<MutableList<Task>> get() = _urgentNotImportantTasks
    val notUrgentNotImportantTasks: LiveData<MutableList<Task>> get() = _notUrgentNotImportantTasks

    // Функция для добавления задачи в выбранную категорию
    fun addTaskToCategory(task: Task) {
        when (task.category) {
            "urgentImportant" -> _urgentImportantTasks.value?.add(task)
            "notUrgentImportant" -> _notUrgentImportantTasks.value?.add(task)
            "urgentNotImportant" -> _urgentNotImportantTasks.value?.add(task)
            "notUrgentNotImportant" -> _notUrgentNotImportantTasks.value?.add(task)
        }
    }

    // Функция для обновления задачи
    fun updateTask(task: Task) {
        val tasksList = when (task.category) {
            "urgentImportant" -> _urgentImportantTasks.value
            "notUrgentImportant" -> _notUrgentImportantTasks.value
            "urgentNotImportant" -> _urgentNotImportantTasks.value
            "notUrgentNotImportant" -> _notUrgentNotImportantTasks.value
            else -> null
        }
        tasksList?.let {
            val index = it.indexOfFirst { it.id == task.id }
            if (index != -1) {
                it[index] = task
            }
        }
    }
}
