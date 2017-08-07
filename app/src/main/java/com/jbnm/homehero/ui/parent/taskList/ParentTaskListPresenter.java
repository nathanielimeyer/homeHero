package com.jbnm.homehero.ui.parent.taskList;

import com.jbnm.homehero.ui.taskEdit.TaskEditorContract;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentTaskListPresenter implements ParentTaskListContract.Presenter {
    private ParentTaskListContract.MvpView mvpView;

    public ParentTaskListPresenter(ParentTaskListContract.MvpView view) { mvpView = view; }

    @Override
    public void detach() {

    }

    @Override
    public void loadChild(String childId) {

    }

    @Override
    public void saveChild() {

    }

    @Override
    public void deleteTaskButtonClicked() {

    }
}
