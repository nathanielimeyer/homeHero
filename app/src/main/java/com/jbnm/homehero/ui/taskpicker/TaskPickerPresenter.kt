package com.jbnm.homehero.ui.taskpicker

import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.data.remote.DataManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class TaskPickerPresenter(val mvpView: TaskPickerContract.MvpView) : TaskPickerContract.Presenter {
    var dataManager: DataManager? = null
    var disposable: CompositeDisposable? = null
    init {
        dataManager = DataManager()
        disposable = CompositeDisposable()
    }

    override fun loadTasks() {
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

    override fun taskSelected(task: Task) {
        mvpView.showSelectedTask(task.description)
    }

}