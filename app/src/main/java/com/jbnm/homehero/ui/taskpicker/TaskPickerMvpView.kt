package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task

interface TaskPickerMvpView {
    fun addTasks(tasks: List<Task>)
}