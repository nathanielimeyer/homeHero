package com.jbnm.homehero.ui.goal;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalPresenter implements GoalContract.Presenter {
    private static final String TAG = "GoalPresenter";
    private GoalContract.MvpView mvpView;
    private Context mContext;
    private Child child;
    private Reward reward;
    private Task task;
    private DataManager dataManager;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ListAdapter adapter;
    private List<Reward> rewards;

    public static class DialogItem{
        public final String text;
        public final int image;
        public DialogItem(String text, int image) {
            this.text = text;
            this.image = image;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    public GoalPresenter(GoalContract.MvpView view, Context context) {
        mvpView = view;
        mContext = context;
        dataManager = new DataManager();
    }

    @Override
    public void detach() {
        mvpView = null;
    }

    @Override
    public void loadData() {
        disposable.add(dataManager.getChild("-Kq5YlmM3saCunGh6Jr_")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override
                    public void onNext(Child childResult) {
                        child = childResult;
                        populateAllTheThings();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {}
                }));
    }

//    @Override
    public void populateAllTheThings() {
        rewards = new ArrayList(child.getRewards().values());
        mvpView.setGoalDescription(child.currentReward().getDescription());
        mvpView.setGoalImage(child.currentReward().getRewardImage());
        checkProgress();
        determineTaskButtonStatus();
        mvpView.buildGoalPickerDialog();
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

    @Override
    public void setNewRewardAndDecrementPoints(int i) {
        Log.d(TAG, "total points = " + child.getTotalPoints());
        child.setTotalPoints(child.getTotalPoints() - child.currentReward().getValue());
        Log.d(TAG, "total points = " + child.getTotalPoints());
        Log.d(TAG, "New Reward Key: " + rewards.get(i).getId());
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
    public ListAdapter goalPickerListAdapter() {
        return null;
    }

    @Override
    public void rewardAnimationEnded() {
        mvpView.showGoalPickerDialog();
    }

    @Override
    public List buildRewardDialogList() {
        List<DialogItem> dialogItems = new ArrayList<>();
        Log.d(TAG, "Called buildRewardDialogList" );
        dialogItems.add(new DialogItem("Disneyland", android.R.drawable.ic_menu_add));
        dialogItems.add(new DialogItem("Camping", android.R.drawable.ic_menu_delete));
        dialogItems.add(new DialogItem("Laser Tag", android.R.drawable.ic_menu_add));

//        rewards = {
//            new Reward("1", "Disneyland", 20, "android.R.drawable.ic_menu_add");
//            new Reward("2", "Camping", 15, "android.R.drawable.ic_menu_delete");
//            new Reward("3", "Laser Tag", 25, "android.R.drawable.arrow_up_float)";
//        }
//        for (Reward reward : rewards) {
//            Log.d(TAG, "Description = " + reward.getDescription());
//            dialogItems.add(new DialogItem(reward.getDescription(), reward.getRewardImage()));
//        }

//        Map<String, Reward> rewardMap = child.getRewards();
//        for (Reward mReward : map.values()) {
//            dialogItems.add(new DialogItem(mReward.getDescription(), mReward.getRewardImage()));
//        }
        return dialogItems;
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }
}
