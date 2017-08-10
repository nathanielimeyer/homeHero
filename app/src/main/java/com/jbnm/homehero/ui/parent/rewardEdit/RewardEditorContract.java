package com.jbnm.homehero.ui.parent.rewardEdit;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by nathanielmeyer on 8/2/17.
 */

public class RewardEditorContract {
    interface Presenter extends BasePresenter {
        void saveChildData(String description, int value);
        void loadChildAndReward(String childId, String rewardId);
        void setRewardIcon(int i);
        List<String> getIconList();
        void cancelButtonClicked();
    }
    interface MvpView extends BaseMvpView {
        void setDescription(String description);
        void loadIcon(String icon);
        void buildIconPickerDialog();
        void parentRewardListIntent(String childId);
        void setValue(int value);
        void showDescriptionError();
    }
}