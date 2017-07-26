package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.data.remote.DataManager
import com.jbnm.homehero.ui.base.BasePresenter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class TaskPickerPresenter(val mvpView: TaskPickerMvpView) : BasePresenter {
    var dataManager: DataManager? = null
    var disposable: CompositeDisposable? = null
    init {
        dataManager = DataManager()
        disposable = CompositeDisposable()
    }

    fun loadTasks() {
        val childId = "-Kpulp2slG8NxvjE3l0u"
        dataManager?.getAllTasks(childId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(getObserver {tasks -> mvpView.addTasks(tasks)})
    }

    inline fun <T> getObserver(crossinline body: (T) -> Unit) = object : Observer<T>{
        override fun onSubscribe(d: Disposable) { disposable?.add(d) }

        override fun onComplete() {}

        override fun onNext(t: T) { body(t) }

        override fun onError(e: Throwable) { e.printStackTrace() }
    }

    override fun detach() {
        disposable?.clear()
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