package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Child
import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.data.remote.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TaskPickerPresenter(val mvpView: TaskPickerContract.MvpView, val childId: String) : TaskPickerContract.Presenter {
    val dataManager: DataManager = DataManager()
    val disposable: CompositeDisposable = CompositeDisposable()

    lateinit var child: Child
    init {
        mvpView.showLoading()

        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ processResult(it) },
                        { processError(it) }))
    }

    fun processResult(childResult: Child) {
        child = childResult
        processTasks(child.tasks.values.toList())
    }

    fun processTasks(tasks: List<Task>) {
        mvpView.addTasks(tasks)
        checkTutorialViewed()
        mvpView.hideLoading()
        mvpView.showTasksCompleted(tasks.filter { !it.availableForSelection() }.size)
    }

    fun processError(error: Throwable) {
        mvpView.hideLoading()
        error.printStackTrace()
        mvpView.showError(error.message)
    }

    fun checkTutorialViewed() {
        // TODO: check if sharedPref for tutorial viewed exists
        mvpView.showTutorial()
    }

    override fun detach() {
        disposable.clear()
    }

    override fun taskSelected(task: Task) {
        child.currentTaskKey = task.id

        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mvpView.showSelectedTask(task) },
                        { processError(it) }))
    }

    override fun handleGoalButtonClick() {
        mvpView.goalProgressIntent(child.id)
    }

    override fun handleTaskButtonClick() {
        mvpView.taskProgressIntent(child.id)
    }

    override fun handleTutorialClick() {
        // TODO: set sharedPref for tutorial viewed
        mvpView.hideTutorial()
    }

}