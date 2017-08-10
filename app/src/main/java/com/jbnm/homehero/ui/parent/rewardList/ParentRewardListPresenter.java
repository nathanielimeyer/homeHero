package com.jbnm.homehero.ui.parent.rewardList;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.remote.DataManager;

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
    private Child child;
    private List<Reward> rewards;

    public ParentRewardListPresenter(ParentRewardListContract.MvpView view) { this.mvpView = view; }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void loadRewards(String childId) {
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
        rewards = new ArrayList<>(child.getRewards().values());
        mvpView.listRewards(rewards);
        mvpView.hideLoading();
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
    public void addNewReward(String description, int value, String rewardImage) {
//        Reward reward = Reward.newInstance(description, value, rewardImage);
//        rewards.add(reward);
//        mvpView.listRewards(rewards);

    }

    public void saveRewards(List<Reward> rewards) {
        Map<String, Reward> rewardMap = new HashMap<>();
        for (Reward reward : rewards) rewardMap.put(reward.getId(), reward);
        child.setRewards(rewardMap);
        saveChild();

    }
}
