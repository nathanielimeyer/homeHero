package com.jbnm.homehero.ui.parent.rewardEdit;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.remote.DataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class RewardEditorPresenter implements RewardEditorContract.Presenter {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DataManager dataManager = new DataManager();

    private RewardEditorContract.MvpView mvpView;
    private List<String> icons = new ArrayList<>(Arrays.asList("goal_ball", "goal_beach", "goal_car", "goal_controller", "goal_dice", "goal_drums", "goal_gun", "goal_kite", "goal_legos"));
    private Child child;
    private List<Reward> rewards;
    private Reward rewardToEdit;

    public RewardEditorPresenter(RewardEditorContract.MvpView view) {
        mvpView = view;
    }

    @Override
    public void detach() {
        disposable.clear();
    }

    @Override
    public void saveChildData(String description, int value) {
        boolean validDescription = validateDescription(description);
        if (validDescription) {
            rewardToEdit.setDescription(description);
            rewardToEdit.setValue(value);
            if (rewardToEdit.getRewardImage().isEmpty()) {
                rewardToEdit.setRewardImage(Constants.DEFAULT_REWARD_ICON);
            }
            disposable.add(dataManager.updateChild(child)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<Child>() {
                        @Override
                        public void onNext(Child childResult) {
                            child = childResult;
                            mvpView.parentRewardListIntent(child.getId());
                        }

                        @Override
                        public void onError(Throwable e) {
                            processError(e);
                        }

                        @Override
                        public void onComplete() {
                        }
                    }));
        }
    }

    @Override
    public void loadChildAndReward(String childId, final String rewardId) {
        if (rewardId.equals(Constants.REWARD_NEW_INTENT_VALUE)) {
            mvpView.setToolbarTitle(Constants.PARENT_REWARD_NEW_TITLE);
        } else {
            mvpView.setToolbarTitle(Constants.PARENT_REWARD_EDIT_TITLE);
        }
        mvpView.showLoading();
        mvpView.buildIconPickerDialog();
        disposable.add(dataManager.getChild(childId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Child>() {
                    @Override public void accept(Child childResult) throws Exception {
                        child = childResult;
                    }
                })
                .subscribeWith(new DisposableObserver<Child>() {
                    @Override public void onNext(Child child) { processResult(child, rewardId); }
                    @Override public void onError(Throwable e) { processError(e); }
                    @Override public void onComplete() {}
                }));
    }


    @Override
    public void setRewardIcon(int i) {
        rewardToEdit.setRewardImage(getIconList().get(i));
        mvpView.loadIcon(rewardToEdit.getRewardImage());
    }

    @Override
    public List<String> getIconList() {
        return icons;
    }

    @Override
    public void cancelButtonClicked() {
        mvpView.parentRewardListIntent(child.getId());
    }

    private void processError(Throwable e) {
        mvpView.hideLoading();
        e.printStackTrace();
        mvpView.showError(e.getMessage());
    }

    private void processResult(Child child, String rewardId) {
        if (!rewardId.equals(Constants.REWARD_NEW_INTENT_VALUE)) {
            rewards = new ArrayList(child.getRewards().values());
            for (Reward reward : rewards) {
                if (reward.getId().equals(rewardId)) {
                    rewardToEdit = reward;
                }
            }
        } else {
            rewardToEdit = Reward.newInstance("", 0, "");
            child.addReward(rewardToEdit);
        }
        mvpView.setDescription(rewardToEdit.getDescription());
        mvpView.setValue(rewardToEdit.getValue());
        if (rewardToEdit.getRewardImage() != null & !rewardToEdit.getRewardImage().equals("")) {
            mvpView.loadIcon(rewardToEdit.getRewardImage());
        } else {
            mvpView.loadIcon("ic_add_a_photo_black_24dp");
        }

        mvpView.hideLoading();
    }

    private boolean validateDescription(String description) {
        if (description.isEmpty()) {
            mvpView.showDescriptionError();
            return false;
        } else {
            return true;
        }
    }
}
