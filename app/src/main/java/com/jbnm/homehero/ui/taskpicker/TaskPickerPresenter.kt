package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Child
import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.data.remote.DataManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TaskPickerPresenter(val mvpView: TaskPickerContract.MvpView) : TaskPickerContract.Presenter {
    val dataManager: DataManager = DataManager()
    val disposable: CompositeDisposable = CompositeDisposable()

    // Temporary, will be passed in to from activity or retrieved based on logged in user
    val childId = "-Kpulp2slG8NxvjE3l0u"
    lateinit var child: Child
    init {
        mvpView.showLoading()

        disposable.add(dataManager.getChild(childId)
                .doOnNext { childResult -> child = childResult }
                .switchMap { childResult -> dataManager.getAllTasksFromList(childResult.tasks.keys.toList()) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ processTasks(it) },
                        { processError(it) }))
    }

    fun processTasks(tasks: List<Task>) {
        mvpView.addTasks(tasks)
        checkTutorialViewed()
        mvpView.hideLoading()
        mvpView.showTasksCompleted(tasks.filter { !it.availableForSelection() }.size, tasks.size)
    }

    fun processError(error: Throwable) {
        // Handle error here
        mvpView.hideLoading()
        error.printStackTrace()
    }

    fun checkTutorialViewed() {
        // check if sharedPref for tutorial viewed exists
        mvpView.showTutorial()
    }

    override fun detach() {
        disposable.clear()
    }

    override fun taskSelected(task: Task) {
        child.currentTask = task.id

        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mvpView.showSelectedTask(task) },
                        { processError(it) }))
    }

    override fun handleGoalButtonClick() {
        mvpView.goalProgressIntent()
    }

    override fun handleTaskButtonClick(task: Task) {
        mvpView.taskProgressIntent(task)
    }

    override fun handleTutorialClick() {
        // set sharedPref for tutorial viewed
        mvpView.hideTutorial()
    }

}