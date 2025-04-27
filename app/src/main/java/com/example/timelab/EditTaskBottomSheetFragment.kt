package com.example.timelab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.view.*
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import java.text.SimpleDateFormat
import java.util.*

class EditTaskBottomSheetFragment(
    private val task: Task,
    private val onTaskUpdated: (Task) -> Unit,
    private val onTaskDeleted: (Task) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateEditText: EditText

    private lateinit var notificationCheckBox: CheckBox
    private lateinit var completedCheckBox: CheckBox
    private lateinit var saveButton: Button

    private val channelId = "task_channel"

    private lateinit var timePicker: TimePicker
    private lateinit var deleteButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        timePicker = view.findViewById(R.id.timePicker)
        deleteButton = view.findViewById(R.id.deleteButton)

        titleEditText = view.findViewById(R.id.titleEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        dateEditText = view.findViewById(R.id.dateEditText)
        notificationCheckBox = view.findViewById(R.id.notificationCheckBox)
        completedCheckBox = view.findViewById(R.id.completedCheckBox)
        saveButton = view.findViewById(R.id.saveButton)


        timePicker.setIs24HourView(true)

        // Установим текущую дату, если пусто
        if (task.date?.isBlank() != false) {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateEditText.setText(sdf.format(Date()))
        } else {
            dateEditText.setText(task.date)
        }
        val calendar = Calendar.getInstance()
        timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timePicker.minute = calendar.get(Calendar.MINUTE)

        titleEditText.setText(task.title)
        descriptionEditText.setText(task.description)
        notificationCheckBox.isChecked = task.notification
        completedCheckBox.isChecked = task.isCompleted

        createNotificationChannel()

        saveButton.setOnClickListener {
            val updatedTask = task.copy(
                title = titleEditText.text.toString(),
                description = descriptionEditText.text.toString(),
                date = dateEditText.text.toString(),
                notification = notificationCheckBox.isChecked,
                isCompleted = completedCheckBox.isChecked
            )

            if (notificationCheckBox.isChecked) {
                val hour = timePicker.hour
                val minute = timePicker.minute
                (activity as? CalendarActivity)?.setTaskReminder(updatedTask, hour, minute)
            }

            onTaskUpdated(updatedTask)
            dismiss()
        }
        deleteButton.setOnClickListener {
            onTaskDeleted(task)
            dismiss()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task Reminder"
            val descriptionText = "Channel for task notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(taskTitle: String) {
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_task_reminder)
            .setContentTitle("Напоминание")
            .setContentText("Не забудь про задачу: $taskTitle")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}

