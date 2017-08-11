package com.jbnm.homehero.ui.taskEdit;

import com.jbnm.homehero.ui.base.BaseMvpView;
import com.jbnm.homehero.ui.base.BasePresenter;

import java.util.List;

/**
 * Created by nathanielmeyer on 8/2/17.
 */

public class TaskEditorContract {
    interface Presenter extends BasePresenter {
        void saveChildData(String description);
        void loadChildAndTask(String childId, String taskId);
        void addStepsButtonClicked();
        void setTaskIcon(int i);
        List<String> getIconList();
        void cancelButtonClicked();
    }
    interface MvpView extends BaseMvpView {
        void setDescription(String description);
        void loadIcon(String icon);
        void setInstructions(List<String> instructions);
        void buildIconPickerDialog();
        void parentTaskListIntent(String childId);
        void showDescriptionError();
    }
}