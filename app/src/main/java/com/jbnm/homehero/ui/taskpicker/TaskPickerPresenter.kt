package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.data.remote.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers

class TaskPickerPresenter(val mvpView: TaskPickerMvpView) {
    var dataManager: DataManager? = null
    init {
        dataManager = DataManager()
    }

    fun loadTasks() {
        val childId = "-Kpulp2slG8NxvjE3l0u"
        dataManager?.getAllTasks(childId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        {tasks: List<Task> -> mvpView.addTasks(tasks)},
                        {e: Throwable -> e.printStackTrace()}
                )
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