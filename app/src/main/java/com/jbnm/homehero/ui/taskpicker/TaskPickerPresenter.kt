package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task

class TaskPickerPresenter(var mvpView: TaskPickerMvpView) {


    fun loadTasks() {
        mvpView.addTasks(generateTasks())
    }

    fun generateTasks(): List<Task> {
        val instructions: List<String> = listOf("step1", "step2")
        val tasks: List<Task> = listOf(
                Task("testId", "Task 1", instructions, true),
                Task("testId", "Task 2", instructions, false),
                Task("testId", "Task 3", instructions, false),
                Task("testId", "Task 4", instructions, true),
                Task("testId", "Task 5", instructions, false),
                Task("testId", "Really long chore description", instructions, true),
                Task("testId", "Task 7", instructions, true),
                Task("testId", "Task 8", instructions, true),
                Task("testId", "Task 9", instructions, true)
        )
        return tasks
    }

}