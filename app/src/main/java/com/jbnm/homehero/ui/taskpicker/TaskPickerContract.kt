package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.ui.base.BaseMvpView
import com.jbnm.homehero.ui.base.BasePresenter

class TaskPickerContract {
    interface Presenter : BasePresenter {
        fun taskSelected(task: Task)
        fun goalButtonClick()
        fun taskButtonClick(task: Task)
    }
    interface MvpView : BaseMvpView {
        fun addTasks(tasks: List<Task>)
        fun showSelectedTask(task: Task)
        fun showTasksCompleted(tasksCompleted: Int, tasksRequired: Int)
        fun taskProgressIntent(task: Task)
        fun goalProgressIntent()
    }
}
