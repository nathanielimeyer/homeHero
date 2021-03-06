package com.jbnm.homehero.ui.parent.taskreview;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.model.TaskStatus;
import com.jbnm.homehero.data.remote.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by janek on 8/1/17.
 */

public class TaskReviewPresenter implements TaskReviewContract.Presenter {
    private TaskReviewContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager = new DataManager();
    private Child child;

    public TaskReviewPresenter(TaskReviewContract.MvpView mvpView) {
        this.mvpView = mvpView;
    }

    public void detach() {
        disposable.clear();
    }

    @Override
    public void loadTasks(String childId) {
        mvpView.showLoading();
        disposable.add(dataManager.getChild(childId)
                .doOnNext(new Consumer<Child>() {
                    @Override public void accept(Child childResult) throws Exception {
                        child = childResult;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void handleTaskApprove(String taskId) {
        child.markTaskApproved(taskId);
        updateChild();
    }

    @Override
    public void handleTaskReject(String taskId) {
        child.markTaskRejected(taskId);
        updateChild();
    }

    private void updateChild() {
        mvpView.showLoading();
        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    private void processResult(Child child) {
        List<Object> items = Observable.fromIterable(child.getTasks().values())
                .collect(new Callable<Map<String, List<Task>>>() {
                    @Override public Map<String, List<Task>> call() throws Exception {
                        return new HashMap<>();
                    }
                }, new BiConsumer<Map<String, List<Task>>, Task>() {
                    @Override public void accept(Map<String, List<Task>> map, Task task) throws Exception {
                        if (!map.containsKey(task.taskStatus().status())) {
                            map.put(task.taskStatus().status(), new ArrayList<Task>());
                        }
                        map.get(task.taskStatus().status()).add(task);
                    }
                }).map(new Function<Map<String,List<Task>>, List<Object>>() {
                    @Override public List<Object> apply(Map<String, List<Task>> stringListMap) throws Exception {
                        List<Object> items = new ArrayList<>();
                        for (TaskStatus key : TaskStatus.values()) {
                            if (stringListMap.containsKey(key.status())) {
                                items.add(key.status());
                                items.addAll(stringListMap.get(key.status()));
                            }
                        }
                        return items;
                    }
                }).blockingGet();

        mvpView.showTasks(items);
        mvpView.hideLoading();
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }
}
