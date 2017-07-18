package com.jbnm.homehero

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.ui.taskwheel.TaskWheel

class MainActivity : AppCompatActivity() {
    lateinit var taskWheel: TaskWheel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskWheel = findViewById(R.id.taskWheel)
        taskWheel.addTasks(generateTasks())
    }

    fun generateTasks(): List<Task> {
        val instructions: List<String> = listOf("step1", "step2")
        val tasks: List<Task> = listOf(
                Task("testId", "Task 1", instructions, true),
                Task("testId", "Task 2", instructions, true),
                Task("testId", "Task 3", instructions, true),
                Task("testId", "Task 4", instructions, true),
                Task("testId", "Task 5", instructions, true),
                Task("testId", "Task 6", instructions, true),
                Task("testId", "Task 7", instructions, true)
        )
        return tasks
    }

}
