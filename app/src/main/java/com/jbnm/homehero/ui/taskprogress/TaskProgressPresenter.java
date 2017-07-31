package com.jbnm.homehero.ui.taskprogress;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.remote.DataManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by janek on 7/30/17.
 */

public class TaskProgressPresenter implements TaskProgressContract.Presenter {
    private CompositeDisposable disposable;
    private DataManager dataManager;

    public TaskProgressPresenter() {
        disposable = new CompositeDisposable();
        dataManager = new DataManager();
    }

    @Override
    public void detach() {
        disposable.clear();
    }


    @Override
    public void loadTask(String childId) {
        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) {
                        
                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onComplete() {

                    }
                }));
    }

    @Override
    public void handleTaskCompleteClick() {

    }
}
