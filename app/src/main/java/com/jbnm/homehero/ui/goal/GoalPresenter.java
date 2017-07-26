package com.jbnm.homehero.ui.goal;

import android.content.Context;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;

import java.util.Arrays;
import java.util.List;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalPresenter implements GoalContract.Presenter {
    private GoalContract.MvpView mvpView;
    private Context mContext;
    private Child child;
    private Reward reward;
    private Task task;

    public GoalPresenter(GoalContract.MvpView view, Context context) {
        mvpView = view;
        mContext = context;

        //replace this with code for loading these objects from firebase
        List<String> instructions = Arrays.asList("change bed", "vacuum walls");
        task = new Task("1", "Wash your room", instructions, true );
        List<Task> tasks = Arrays.asList(task);
        reward = new Reward("1", "Disneyland", 10, "disneycastle");
        List<Reward> rewards = Arrays.asList(reward);
        child = new Child("1", tasks, rewards);
        child.setTotalPoints(10);
        //replace this with code for loading these objects from firebase

    }

    @Override
    public void detach() {
        mvpView = null;
    }

    @Override
    public void loadData() {
        mvpView.setGoalDescription(reward.getDescription());
        mvpView.setGoalImage(reward.getRewardImage());
    }

    @Override
    public void checkProgress() {
        if (child.getTotalPoints() >= reward.getValue()) {
            mvpView.showProgress(reward.getDescription(), reward.getValue(), reward.getRewardImage(), child.getTotalPoints(), child.calculatePendingPoints());
            mvpView.showRewardAnimation();
        } else {
            mvpView.showProgress(reward.getDescription(), reward.getValue(), reward.getRewardImage(), child.getTotalPoints(), child.calculatePendingPoints());
        }
    }

    @Override
    public void taskButtonClicked() {
        if (child.getCurrentTask() == null) {
            mvpView.taskPickerIntent();
        } else {
            mvpView.taskProgressIntent();
        }
    }

    @Override
    public void determineTaskButtonStatus() {
        if (child.allTasksCompleted()) {
            mvpView.hideTaskButton();
        } else {
            mvpView.showTaskButton();
        }
    }
}

