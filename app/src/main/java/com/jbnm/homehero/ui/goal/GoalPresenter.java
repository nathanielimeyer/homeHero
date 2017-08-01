package com.jbnm.homehero.ui.goal;

import android.content.Context;
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
import java.util.List;
import java.util.Map;

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

        //replace this with code for loading these objects from firebase
        List<String> instructions = Arrays.asList("change bed", "vacuum walls");
        task = new Task("1", "Wash your room", instructions, true);
        List<Task> tasks = Arrays.asList(task);
        reward = new Reward("1", "Disneyland", 10, "disneycastle");
        List<Reward> rewards = Arrays.asList(reward);
        child = new Child("1");

//        child = dataManager.getChild("-Kpulp2slG8NxvjE3l0u");
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
        child.setTotalPoints(child.getTotalPoints() - reward.getValue());
        reward = new Reward("1", "Camping", 10, "camping");
        loadData();
        checkProgress();
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
//        Map<String, Reward> map = child.getRewards();
//        for (Reward mReward : map.values()) {
//            dialogItems.add(new DialogItem(mReward.getDescription(), mReward.getRewardImage()));
//            Log.d(TAG, "Description = " + mReward.getDescription());
//        }
        return dialogItems;
    }
}
