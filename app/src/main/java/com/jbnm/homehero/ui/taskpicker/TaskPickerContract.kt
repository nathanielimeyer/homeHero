package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.ui.base.BaseMvpView
import com.jbnm.homehero.ui.base.BasePresenter

class TaskPickerContract {
    interface Presenter : BasePresenter {
        fun loadTasks()
        fun taskSelected(task: Task)
    }
    interface MvpView : BaseMvpView {
        fun addTasks(tasks: List<Task>)
        fun showSelectedTask(task: String)
        fun showTasksCompleted(tasksCompleted: Int, tasksRequired: Int)
        fun taskProgressIntent()
        fun goalProgressIntent()
    }
}
