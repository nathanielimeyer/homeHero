package com.jbnm.homehero.ui.taskprogress;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.remote.DataManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by janek on 7/30/17.
 */

public class TaskProgressPresenter implements TaskProgressContract.Presenter {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager = new DataManager();
    private Child child;
    private TaskProgressContract.MvpView mvpView;

    public TaskProgressPresenter(TaskProgressContract.MvpView mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void loadTask(String childId) {
        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Child>() {
                    @Override public void accept(Child childResult) throws Exception {
                        child = childResult;
                    }
                })
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void handleTaskCompleteClick() {
        if (child.getCurrentTaskKey() != null) {
            child.currentTask().markTaskComplete();
        }
        if (child.getRejectedTasks().size() > 0) {
            child.setCurrentTaskKey(child.getRejectedTasks().remove(0));
        } else {
            child.setCurrentTaskKey(null);
        }
        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
        // mark current task as complete - put it into pending state
        // check queue for rejected tasks
        // - if tasks in queue set new current task and reload page
        // - else set current task to null and go to task progress screen
    }

    private void processResult(Child child) {
        if (child.getCurrentTaskKey() == null) {
            mvpView.goalProgressIntent();
        } else {
            mvpView.showTask(child.currentTask());
        }
    }

    private void processError(Throwable e) {
        e.printStackTrace();
        // Handle error here
    }
}
