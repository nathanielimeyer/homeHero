package com.jbnm.homehero.ui.parent;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

/**
 * Created by janek on 8/7/17.
 */

public class ParentContract {
    interface Presenter extends BasePresenter {
        void init(String childId);
        void handleChildNavButtonClick();
    }
    interface  MvpView extends BaseMvpView {
        void setUpViewPager(String childId);
        void taskListIntent(String childId);
        void rewardListIntent(String childId);
        void goalIntent(String childId);
    }
}
