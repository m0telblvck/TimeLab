package com.example.timelab

data class Task(
    var id: Long = 0,
    var title: String,
    var description: String,
    var category: String = "",
    var isCompleted: Boolean = false,
    var date: String? = null,
    var duration: String? = null,
    var deadline: String? = null,
    var notification: Boolean = false,
    var dueDate: String? = null,
    var isDone: Boolean = false
)
