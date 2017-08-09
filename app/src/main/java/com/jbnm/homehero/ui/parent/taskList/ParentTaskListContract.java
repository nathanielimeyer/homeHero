package com.jbnm.homehero.ui.parent.taskList;

import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentTaskListContract {
    interface Presenter extends BasePresenter {
        void loadTasks(String childId);
        void saveChild();
        void addTaskButtonClicked();
        void saveTasks(List<Task> tasks);
        }
    interface MvpView extends BaseMvpView {
        void listTasks(List<Task> tasks);
        void addTaskIntent(String childId);
    }
}
