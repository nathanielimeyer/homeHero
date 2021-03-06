package com.jbnm.homehero.ui.taskEdit;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class TaskEditorPresenter implements TaskEditorContract.Presenter {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager = new DataManager();

    private TaskEditorContract.MvpView mvpView;
    private List<String> icons = new ArrayList<>(Arrays.asList("chore_broom", "chore_laundry", "chore_pencil", "chore_trash", "chore_vacuum"));
    private Child child;
    private List<Task> tasks;
    private List<String> instructions;
    private Task taskToEdit;

    public TaskEditorPresenter(TaskEditorContract.MvpView view) {
        mvpView = view;
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
//    public void saveChildData(String description, String icon, List<String> instructions) {
    public void saveChildData(String description) {
        boolean validDescription = validateDescription(description);

        if (validDescription) {
            taskToEdit.setDescription(description);
            if (taskToEdit.getIcon().isEmpty()) {
                taskToEdit.setIcon(Constants.DEFAULT_CHORE_ICON);
            }
            taskToEdit.getInstructions().remove("");
            disposable.add(dataManager.updateChild(child)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Child>() {
                        @Override
                        public void onNext(Child childResult) {
                            child = childResult;
                            mvpView.parentTaskListIntent(child.getId());
                        }

                        @Override
                        public void onError(Throwable e) {
                            processError(e);
                        }

                        @Override
                        public void onComplete() {
                        }
                    }));
        }
    }

    @Override
    public void loadChildAndTask(String childId, final String taskId) {
        if (taskId.equals(Constants.TASK_NEW_INTENT_VALUE)) {
            mvpView.setToolbarTitle(Constants.PARENT_TASK_NEW_TITLE);
        } else {
            mvpView.setToolbarTitle(Constants.PARENT_TASK_EDIT_TITLE);
        }
        mvpView.showLoading();
        mvpView.buildIconPickerDialog();
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

    @Override
    public void addStepsButtonClicked() {
        instructions.add("");
        mvpView.setInstructions(instructions);
    }

    @Override
    public void setTaskIcon(int i) {
        taskToEdit.setIcon(getIconList().get(i));
        mvpView.loadIcon(taskToEdit.getIcon());
    }

    @Override
    public List<String> getIconList() {
        return icons;
    }

    @Override
    public void cancelButtonClicked() {
        mvpView.parentTaskListIntent(child.getId());
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }

    private void processResult(Child child, String taskId) {
        if (!taskId.equals(Constants.TASK_NEW_INTENT_VALUE)) {
            tasks = new ArrayList(child.getTasks().values());
            for (Task task : tasks) {
                if (task.getId().equals(taskId)) {
                    taskToEdit = task;
                }
            }
        } else {
            instructions = new ArrayList<>();
            instructions.add("");
            taskToEdit = Task.newInstance("", "", instructions);
            child.addTask(taskToEdit);
        }
        mvpView.setDescription(taskToEdit.getDescription());
        if (taskToEdit.getIcon() != null & !taskToEdit.getIcon().equals("")) {
            mvpView.loadIcon(taskToEdit.getIcon());
        } else {
            mvpView.loadIcon("ic_add_a_photo_black_24dp");
        }
        instructions = taskToEdit.getInstructions();
        mvpView.setInstructions(instructions);

        mvpView.hideLoading();
    }

    private boolean validateDescription(String description) {
        if (description.isEmpty()) {
            mvpView.showDescriptionError();
            return false;
        } else {
            return true;
        }
    }
}
