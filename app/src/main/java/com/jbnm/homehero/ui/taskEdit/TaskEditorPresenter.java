package com.jbnm.homehero.ui.taskEdit;


import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class TaskEditorPresenter implements TaskEditorContract.Presenter {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager = new DataManager();

    private TaskEditorContract.MvpView mvpView;
    private Child child;
    private List<Task> tasks;
    private Task taskToEdit;


    public TaskEditorPresenter(TaskEditorContract.MvpView view) {
        mvpView = view;
    }

    @Override
    public void detach() {

    }

    @Override
//    public void saveChildData(String description, String icon, List<String> instructions) {
    public void saveChildData(String description) {
        taskToEdit.setDescription(description);
//        taskToEdit.setIcon(icon);
//        taskToEdit.setInstructions(instructions);
        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child childResult ) {
                        child = childResult;
                    }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void loadChildAndTask(String childId, final String taskId) {
        mvpView.showLoading();
        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Child>() {
                    @Override public void accept(Child childResult) throws Exception {
                        child = childResult;
                    }
                })
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child, taskId); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }

    private void processResult(Child child, String taskId) {
        if (!taskId.equals("newTask")) {
            tasks = new ArrayList(child.getTasks().values());
            for (Task task : tasks) {
                if (task.getId().equals(taskId)) {
                    taskToEdit = task;
                }
            }
            mvpView.setDescription(taskToEdit.getDescription());
            mvpView.setInstructions(taskToEdit.getInstructions());
        }
        mvpView.hideLoading();
    }

}

