package com.jbnm.homehero.ui.parent.taskreview;

import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by janek on 8/1/17.
 */

public class TaskReviewContract {
    interface Presenter extends BasePresenter {
        void loadTasks(String childId);
        void handleTaskApprove(String taskId);
        void handleTaskReject(String taskId);
    }
    interface MvpView extends BaseMvpView{
        void showTasks(List<Object> tasks);
    }
}
