package com.jbnm.homehero.ui.goal;

import android.widget.ListAdapter;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class GoalPresenter implements GoalContract.Presenter {
    private static final String TAG = "GoalPresenter";
    private GoalContract.MvpView mvpView;
    private Child child;
    private Reward reward;
    private Task task;
    private DataManager dataManager;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ListAdapter adapter;
    private List<Reward> rewards;

    public static class DialogItem{
        public final String text;
        public final String image;
        public DialogItem(String text, String image) {
            this.text = text;
            this.image = image;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    public GoalPresenter(GoalContract.MvpView view) {
        mvpView = view;
        dataManager = new DataManager();
    }

    @Override
    public void detach() {
        mvpView = null;
        disposable.clear();
    }

    @Override
    public void loadData(String childId) {
        mvpView.showLoading();
        mvpView.setToolbarTitle(Constants.GOAL_PROGRESS_TITLE);
        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override
                    public void onNext(Child childResult) {
                        child = childResult;
                        populateAllTheThings();
                    }

                    @Override
                    public void onError(Throwable e) {
                        processError(e);
                    }

                    @Override
                    public void onComplete() {}
                }));
    }

    @Override
    public void populateAllTheThings() {
        rewards = new ArrayList(child.getRewards().values());
        mvpView.buildGoalPickerDialog();
        determineTaskButtonStatus();
        if (child.getCurrentRewardKey() == null) {
            mvpView.showGoalPickerDialog();
        } else {
            mvpView.hideLoading();
            mvpView.setGoalDescription(child.currentReward().getDescription());
            mvpView.setGoalImage(child.currentReward().getRewardImage());
            checkProgress();
            int remainingPoints = child.currentReward().getValue() - child.getTotalPoints();
            if (remainingPoints < 0) {
                remainingPoints = 0;
            }
            mvpView.setGoalProgressLabel(remainingPoints);
        }
    }
    @Override
    public void checkProgress() {
        if (child.getTotalPoints() >= child.currentReward().getValue()) {
            mvpView.showProgress(child.currentReward().getDescription(), child.currentReward().getValue(), child.currentReward().getRewardImage(), child.getTotalPoints(), child.calculatePendingPoints());
            mvpView.showRewardAnimation();
        } else {
            mvpView.showProgress(child.currentReward().getDescription(), child.currentReward().getValue(), child.currentReward().getRewardImage(), child.getTotalPoints(), child.calculatePendingPoints());
        }
    }

    @Override
    public void taskButtonClicked() {
        if (child.currentTask() == null) {
            mvpView.taskPickerIntent(child.getId());
        } else {
            mvpView.taskProgressIntent(child.getId());
        }
    }

    @Override
    public void determineTaskButtonStatus() {
        if (child.allTasksCompleted()) {
            mvpView.hideTaskButton();
            mvpView.showNoTasksAvailable();
        } else {
            mvpView.showTaskButton();
            mvpView.hideNoTasksAvailable();
        }

        if (child.currentTask() == null) {
            mvpView.setTaskButtonText(Constants.GOAL_PROGRESS_TASK_SELECT);
        } else {
            mvpView.setTaskButtonText(Constants.GOAL_PROGRESS_TASK_PROGRESS);
        }
    }

    @Override
    public void setNewRewardAndDecrementPoints(int i) {
        child.redeemReward();
        child.setCurrentRewardKey(rewards.get(i).getId());
        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child childResult ) {
                        child = childResult;
                        populateAllTheThings();
                    }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }
    @Override
    public void rewardAnimationEnded() {
        mvpView.showGoalPickerDialog();
    }

    @Override
    public List buildRewardDialogList() {
        List<DialogItem> dialogItems = new ArrayList<>();
        for (Reward reward : rewards) {
            dialogItems.add(new DialogItem(reward.getDescription() + ": " + reward.getValue() + " pts.", reward.getRewardImage()));
        }

        return dialogItems;
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }

    @Override
    public void handleParentNavButtonClick() {
        if (child != null) {
            mvpView.parentIntent(child.getId());
        }
    }
}
