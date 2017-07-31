package com.jbnm.homehero.ui.taskprogress;

import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

public class TaskProgressContract {
    interface Presenter extends BasePresenter {
        void loadTask(String childId);
        void handleTaskCompleteClick();
    }
    interface MvpView extends BaseMvpView {
        void showTask(Task task);

    }
}
