package com.jbnm.homehero.data.remote;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

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

    public Observable<Child> saveChild(Child child) {
        return Observable.combineLatest(firebaseService.saveChild(child.getId(), child),
                firebaseService.addChildToParent(parentId, child.getId()),
                new BiFunction<Child, String, Child>() {
                    @Override
                    public Child apply(Child child, String s) throws Exception {
                        return child;
                    }
                });
    }

    public Observable<Child> getChild(String childId) {
        return firebaseService.getChildById(childId);
    }

    public Observable<Task> saveTask(String childId, Task task) {
        return Observable.combineLatest(firebaseService.saveTask(task.getId(), task),
                firebaseService.addTaskToChild(childId, task.getId(), true),
                new BiFunction<Task, Boolean, Task>() {
                    @Override
                    public Task apply(Task task, Boolean aBoolean) throws Exception {
                        return task;
                    }
                });
    }

    public Observable<Task> getTask(String taskId) {
        return firebaseService.getTaskById(taskId);
    }

    public Observable<List<Task>> getAllTasks(String childId) {
        return firebaseService.getChildById(childId).switchMap(new Function<Child, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Child child) throws Exception {
                return Observable.fromIterable(new ArrayList<String>(child.getTasks().keySet()));
            }
        }).flatMap(new Function<String, ObservableSource<Task>>() {
            @Override
            public ObservableSource<Task> apply(String s) throws Exception {
                return firebaseService.getTaskById(s);
            }
        }).toList().toObservable();
    }

    public Observable<Reward> saveReward(String childId, Reward reward) {
        return Observable.combineLatest(firebaseService.saveReward(reward.getId(), reward),
                firebaseService.addRewardsToChild(childId, reward.getId(), true),
                new BiFunction<Reward, Boolean, Reward>() {
                    @Override
                    public Reward apply(Reward reward, Boolean aBoolean) throws Exception {
                        return reward;
                    }
                });
    }

    public Observable<Reward> getReward(String rewardId) {
        return firebaseService.getRewardById(rewardId);
    }

    public Observable<List<Reward>> getAllRewards(String childId) {
        return firebaseService.getChildById(childId).switchMap(new Function<Child, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Child child) throws Exception {
                return Observable.fromIterable(new ArrayList<String>(child.getRewards().keySet()));
            }
        }).flatMap(new Function<String, ObservableSource<Reward>>() {
            @Override
            public ObservableSource<Reward> apply(String s) throws Exception {
                return firebaseService.getRewardById(s);
            }
        }).toList().toObservable();
    }
}
