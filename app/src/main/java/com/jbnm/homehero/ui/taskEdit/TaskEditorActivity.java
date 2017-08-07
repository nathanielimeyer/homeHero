package com.jbnm.homehero.ui.taskEdit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.goal.GoalPresenter;
import com.jbnm.homehero.ui.parent.taskList.ParentTaskListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskEditorActivity extends BaseActivity implements TaskEditorContract.MvpView {
    @BindView(R.id.edit_text_description) EditText descriptionEditText;
    @BindView(R.id.task_image_view) ImageView task_image_view;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.taskInstructionsRecyclerView) RecyclerView taskInstructionsRecyclerView;
    @BindView(R.id.addStepsButton) Button addStepsButton;
    @BindView(R.id.cancelButton) Button cancelButton;

    private Context context = this;
    ListAdapter adapter;


    private TaskEditorContract.Presenter presenter;

    public static Intent createIntent(Context context, String childId, String taskId) {
        Intent intent = new Intent(context, TaskEditorActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, childId);
        intent.putExtra(Constants.TASK_INTENT_KEY, taskId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);
        ButterKnife.bind(this);

        String childId = getIntent().getStringExtra("childId");

//        String taskId = getIntent().getStringExtra("taskId");
        String taskId = Constants.TASKID;
//        String taskId = "newTask";

        presenter = new TaskEditorPresenter(this);
        presenter.loadChildAndTask(childId, taskId);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskData();
            }
        });
        task_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIconPickerDialog();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.cancelButtonClicked();
            }
        });

        addStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addStepsButtonClicked();
            }
        });
    }

    public void showIconPickerDialog() {
//        List<String> dialogItemList = presenter.getIconList();
//        final String[] dialogItems = new String[dialogItemList.size()];
//        dialogItemList.toArray(dialogItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.icon_picker_title)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.setTaskIcon(i);
                    }
                });
        builder.create().show();
    }

    @Override
    public void buildIconPickerDialog() {
        List<String> dialogItemList = presenter.getIconList();
        final String[] dialogItems = new String[dialogItemList.size()];
        dialogItemList.toArray(dialogItems);

        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                dialogItems){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                //Put the image on the TextView

                tv.setCompoundDrawablesWithIntrinsicBounds((getResources().getIdentifier(dialogItems[position], "drawable", getPackageName())), 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };
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
        task_image_view.setImageResource(getResources().getIdentifier(icon, "drawable", getPackageName()));
    }

    @Override
    public void setInstructions(List<String> instructions) {
        taskInstructionsRecyclerView.setAdapter(new TaskEditorInstructionsAdapter(instructions));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskInstructionsRecyclerView.setLayoutManager(layoutManager);
        taskInstructionsRecyclerView.setHasFixedSize(true);
        taskInstructionsRecyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

    }

    @Override
    public void parentTaskListIntent(String childId) {
        startActivity(ParentTaskListActivity.createIntent(this, childId));
    }
}
