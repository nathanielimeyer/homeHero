package com.jbnm.homehero.ui.parent.taskList;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentTaskListContract {
    interface Presenter extends BasePresenter {
        void loadChild(String childId);
        void saveChild();
        void deleteTaskButtonClicked();
    }
    interface MvpView extends BaseMvpView {
        void setDescriptions(List<String> descriptions);
        void loadIcons(List<String> icons);

    }
}
