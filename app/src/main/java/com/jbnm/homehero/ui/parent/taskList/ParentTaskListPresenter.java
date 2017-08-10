package com.jbnm.homehero.ui.parent.taskList;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.model.TaskStatus;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.util.SharedPrefManager;

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
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentTaskListPresenter implements ParentTaskListContract.Presenter {
    private ParentTaskListContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager = new DataManager();
    private SharedPrefManager sharedPrefManager;
    private Child child;
    private List<Task> tasks;

    public ParentTaskListPresenter(ParentTaskListContract.MvpView view, SharedPrefManager sharedPrefManager) {
        this.mvpView = view;
        this.sharedPrefManager = sharedPrefManager;
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void loadTasks(String childId) {
        mvpView.setToolbarTitle(Constants.PARENT_TASK_LIST_TITLE);
        mvpView.showLoading();
        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Child>() {
                    @Override
                    public void accept(Child childResult) throws Exception {
                        child = childResult;
                    }
                }).subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }

    private void processResult(Child child) {
        updateSharedPrefs(child);
        List<Task> items = new ArrayList<>(child.getTasks().values());
        mvpView.listTasks(items);
        mvpView.hideLoading();
    }

    private void updateSharedPrefs(Child child) {
        int taskCount = child.getTasks().keySet().size();
        mvpView.setAddTaskButtonEnabled(taskCount < Constants.MAX_TASK_COUNT);

        if (taskCount < Constants.MIN_TASK_COUNT) {
            mvpView.showError(String.format("You need to add at least %d tasks", Constants.MIN_TASK_COUNT));
        }

        sharedPrefManager.setTasksCreated(taskCount >= Constants.MIN_TASK_COUNT);
        sharedPrefManager.setGoalsCreated(child.getRewards().keySet().size() >= Constants.MIN_REWARD_COUNT);
    }

    @Override
    public void saveChild() {
        mvpView.showLoading();
        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void addTaskButtonClicked() {
        mvpView.addTaskIntent(child.getId());
    }

    public void saveTasks(List<Task> tasks) {
        Map<String, Task> taskMap = new HashMap<>();
        for (Task task : tasks) taskMap.put(task.getId(), task);
        child.setTasks(taskMap);
        saveChild();

    }

    @Override
    public void handleBackButtonPressed() {
        mvpView.parentIntent(child.getId());
    }
}
