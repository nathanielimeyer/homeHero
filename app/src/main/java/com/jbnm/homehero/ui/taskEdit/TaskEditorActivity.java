package com.jbnm.homehero.ui.taskEdit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.taskEdit.TaskEditorInstructionsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskEditorActivity extends BaseActivity implements TaskEditorContract.MvpView {
    @BindView(R.id.edit_text_description) EditText descriptionEditText;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.taskInstructionsRecyclerView) RecyclerView taskInstructionsRecyclerView;
    private List<EditText> instructionTexts = new ArrayList<>();


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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskData();
            }
        });


    }

    public void updateTaskData() {
        String description = descriptionEditText.getText().toString().trim();

        //        presenter.saveChildData(description, icon, instructions);
        presenter.saveChildData(description);
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
        taskInstructionsRecyclerView.setAdapter(new TaskEditorInstructionsAdapter(instructions));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskInstructionsRecyclerView.setLayoutManager(layoutManager);
        taskInstructionsRecyclerView.setHasFixedSize(true);
        taskInstructionsRecyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
//        for (instruction : instructions) {
//
//        }

    }
}
