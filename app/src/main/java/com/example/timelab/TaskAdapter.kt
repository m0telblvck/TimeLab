package com.example.timelab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(

    val tasks: MutableList<Task>,
    private val onTaskClick: (Task) -> Unit,

) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
        holder.itemView.setOnClickListener {
            onTaskClick(task)

        }

    }

    override fun getItemCount() = tasks.size



    fun submitList(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionView: TextView = itemView.findViewById(R.id.taskDescription)
        private val checkBox: CheckBox = itemView.findViewById(R.id.taskStatus)
        private val taskDate: TextView = itemView.findViewById(R.id.taskDate)


        fun bind(task: Task) {
            descriptionView.text = task.title
            checkBox.isChecked = task.isCompleted

            taskDate.text = task.dueDate ?: "Без даты"

            checkBox.isClickable = false
            checkBox.isFocusable = false

        }
    }
}
