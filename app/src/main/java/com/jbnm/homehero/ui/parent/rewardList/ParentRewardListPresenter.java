package com.jbnm.homehero.ui.parent.rewardList;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.util.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentRewardListPresenter implements ParentRewardListContract.Presenter {
    private ParentRewardListContract.MvpView mvpView;
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager = new DataManager();
    private SharedPrefManager sharedPrefManager;
    private Child child;

    public ParentRewardListPresenter(ParentRewardListContract.MvpView view, SharedPrefManager sharedPrefManager) {
        this.mvpView = view;
        this.sharedPrefManager = sharedPrefManager;
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void loadRewards(String childId) {
        mvpView.setToolbarTitle(Constants.PARENT_REWARD_LIST_TITLE);
        mvpView.showLoading();
        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Child>() {
                    @Override
                    public void accept(Child childResult) throws Exception {
                        child = childResult;
                    }
                }).subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }

    private void processResult(Child child) {
        updateSharedPrefs(child);
        List<Reward> items = new ArrayList<>(child.getRewards().values());
        mvpView.listRewards(items);
        mvpView.hideLoading();
    }

    private void updateSharedPrefs(Child child) {
        sharedPrefManager.setTasksCreated(child.getTasks().keySet().size() >= Constants.MIN_TASK_COUNT);
        sharedPrefManager.setGoalsCreated(child.getRewards().keySet().size() >= Constants.MIN_REWARD_COUNT);
    }

    @Override
    public void saveChild() {
        mvpView.showLoading();
        disposable.add(dataManager.updateChild(child)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }

    @Override
    public void addRewardButtonClicked() {
        mvpView.addRewardIntent(child.getId());
    }

    public void saveRewards(List<Reward> rewards) {
        Map<String, Reward> rewardMap = new HashMap<>();
        for (Reward reward : rewards) rewardMap.put(reward.getId(), reward);
        child.setRewards(rewardMap);
        saveChild();

    }

    @Override
    public void handleBackButtonPressed() {
        mvpView.parentIntent(child.getId());
    }
}
