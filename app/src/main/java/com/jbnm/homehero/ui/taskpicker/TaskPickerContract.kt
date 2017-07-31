package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.ui.base.BaseMvpView
import com.jbnm.homehero.ui.base.BasePresenter

class TaskPickerContract {
    interface Presenter : BasePresenter {
        fun taskSelected(task: Task)
        fun handleGoalButtonClick()
        fun handleTaskButtonClick()
        fun handleTutorialClick()
    }
    interface MvpView : BaseMvpView {
        fun addTasks(tasks: List<Task>)
        fun showSelectedTask(task: Task)
        fun showTasksCompleted(tasksCompleted: Int)
        fun taskProgressIntent(childId: String)
        fun goalProgressIntent()
        fun showTutorial()
        fun hideTutorial()
    }
}
