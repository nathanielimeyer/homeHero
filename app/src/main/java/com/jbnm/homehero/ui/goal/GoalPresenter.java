package com.jbnm.homehero.ui.goal;

import android.content.Context;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalPresenter implements GoalContract.Presenter {
    private GoalContract.MvpView mvpView;
    private Context mContext;
    private Child child;
    private Reward reward;
    private Task task;
    private DataManager dataManager;


    public GoalPresenter(GoalContract.MvpView view, Context context) {
        mvpView = view;
        mContext = context;

//        String childId = "-Kpulp2slG8NxvjE3l0u";
//        dataManager.getChild(childId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Child>() {
//            @Override
//            public void onSubscribe(Disposable d) {}
//            @Override
//            public void onNext(Child resultChild) {
//                child = resultChild;
//
//            }
//            @Override
//            public void onError(Throwable e) { e.printStackTrace(); }
//            @Override
//            public void onComplete() {}
//        });

//        dataManager.getAllTasks(childId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Task>>() {
//            @Override
//            public void onSubscribe(Disposable d) {}
//            @Override
//            public void onNext(List<Task> tasks) {}
//            @Override
//            public void onError(Throwable e) { e.printStackTrace(); }
//            @Override
//            public void onComplete() {}
//        });

        //replace this with code for loading these objects from firebase
        List<String> instructions = Arrays.asList("change bed", "vacuum walls");
        task = new Task("1", "Wash your room", instructions, true);
        List<Task> tasks = Arrays.asList(task);
        reward = new Reward("1", "Disneyland", 10, "disneycastle");
        List<Reward> rewards = Arrays.asList(reward);
        child = new Child("1");
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
//        if (child.getTotalPoints() >= reward.getValue()) {
            mvpView.showProgress(reward.getDescription(), reward.getValue(), reward.getRewardImage(), child.getTotalPoints(), child.calculatePendingPoints());
            mvpView.showRewardAnimation();
//        } else {
//            mvpView.showProgress(reward.getDescription(), reward.getValue(), reward.getRewardImage(), child.getTotalPoints(), child.calculatePendingPoints());
//        }
    }

    @Override
    public void taskButtonClicked() {
//        if (child.getCurrentTask() == null) {
            mvpView.taskPickerIntent();
//        } else {
//            mvpView.taskProgressIntent();
//        }
    }

    @Override
    public void determineTaskButtonStatus() {
//        if (child.allTasksCompleted()) {
//            mvpView.hideTaskButton();
//        } else {
            mvpView.showTaskButton();
//        }
    }
}

