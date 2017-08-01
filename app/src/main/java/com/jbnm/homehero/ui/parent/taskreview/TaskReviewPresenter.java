package com.jbnm.homehero.ui.parent.taskreview;

/**
 * Created by janek on 8/1/17.
 */

public class TaskReviewPresenter implements TaskReviewContract.Presenter {
    private TaskReviewContract.MvpView mvpView;

    public TaskReviewPresenter(TaskReviewContract.MvpView mvpView) {
        this.mvpView = mvpView;
    }

    public void detach() {

    }
}
