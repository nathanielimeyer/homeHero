package com.jbnm.homehero.ui.parent.rewardList;

import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentRewardListContract {
    interface Presenter extends BasePresenter {
        void loadRewards(String childId);
        void saveChild();
        void addRewardButtonClicked();
        void saveRewards(List<Reward> rewards);
        }
    interface MvpView extends BaseMvpView {
        void listRewards(List<Reward> rewards);
        void addRewardIntent(String childId);
    }
}
