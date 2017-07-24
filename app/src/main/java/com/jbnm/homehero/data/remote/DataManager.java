package com.jbnm.homehero.data.remote;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by janek on 7/24/17.
 */

public class DataManager {

    private final FirebaseService.FirebaseAPI firebaseService;

    // Placeholder until auth is implemented
    private String parentId = "ParentTestId";

    public DataManager() {
        firebaseService = FirebaseService.createService(FirebaseService.FirebaseAPI.class);
    }

    public Observable<Parent> saveParent(Parent parent) {
        return firebaseService.saveParent(parentId, parent);
    }

    public Observable<Parent> getParent() {
        return firebaseService.getParentById(parentId);
    }

    public Observable<String> createChild(Child child) {
        return firebaseService.createChild(child).switchMap(new Function<Map<String, String>, Observable<String>>() {
            @Override
            public Observable<String> apply(Map<String, String> stringStringMap) throws Exception {
                return firebaseService.addChildToParent(parentId, stringStringMap.get("name"));
            }
        });
    }

    public Observable<Child> getChild(String childId) {
        return firebaseService.getChildById(childId);
    }

    public Observable<Boolean> saveTask(final String childId, Task task) {
        return firebaseService.createTask(task).switchMap(new Function<Map<String, String>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> apply(Map<String, String> stringStringMap) throws Exception {
                return firebaseService.addTaskToChild(childId, stringStringMap.get("name"), true);
            }
        });
    }

    public Observable<Task> getTask(String taskId) {
        return firebaseService.getTaskById(taskId);
    }

    public Observable<Boolean> saveReward(final String childId, Reward reward) {
        return firebaseService.createReward(reward).switchMap(new Function<Map<String, String>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> apply(Map<String, String> stringStringMap) throws Exception {
                return firebaseService.addRewardsToChild(childId, stringStringMap.get("name"), true);
            }
        });
    }

    public Observable<Reward> getReward(String rewardId) {
        return firebaseService.getRewardById(rewardId);
    }
}
