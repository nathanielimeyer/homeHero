package com.jbnm.homehero.ui.taskEdit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskEditorActivity extends BaseActivity implements TaskEditorContract.MvpView {
    @BindView(R.id.edit_text_description) EditText descriptionEditText;


    private TaskEditorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);
        ButterKnife.bind(this);

        String childId = getIntent().getStringExtra("childId");
//        String taskId = getIntent().getStringExtra("taskId");
        String taskId = Constants.TASKID;

        presenter = new TaskEditorPresenter(this);
        presenter.loadChildAndTask(childId, taskId);

    }

    @Override
    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            descriptionEditText.setText(description);
        } else {
            descriptionEditText.setHint("Tap to edit");
        }
    }

    @Override
    public void loadIcon(String icon) {

    }

    @Override
    public void setInstructions(List<String> instructions) {

    }
}
