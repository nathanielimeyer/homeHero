package com.jbnm.homehero.ui.taskEdit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.parent.taskList.ParentTaskListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskEditorActivity extends BaseActivity implements TaskEditorContract.MvpView {
    @BindView(R.id.edit_text_description) EditText descriptionEditText;
    @BindView(R.id.task_image_view) ImageView task_image_view;
    @BindView(R.id.taskInstructionsRecyclerView) RecyclerView taskInstructionsRecyclerView;
    @BindView(R.id.addStepsButton) FloatingActionButton addStepsButton;

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

        String childId = getIntent().getStringExtra(Constants.CHILD_INTENT_KEY);

        String taskId = getIntent().getStringExtra(Constants.TASK_INTENT_KEY);

        presenter = new TaskEditorPresenter(this);
        presenter.loadChildAndTask(childId, taskId);

        task_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIconPickerDialog();
            }
        });

        addStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addStepsButtonClicked();
            }
        });

        taskInstructionsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_INDICATOR_TOP) {
                    addStepsButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && addStepsButton.isShown()) {
                    addStepsButton.hide();
                } else if (dy < 0 && !addStepsButton.isShown()) {
                    addStepsButton.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_task_edit_save) {
            presenter.saveChildData(descriptionEditText.getText().toString().trim());
            return true;
        } else if (item.getItemId() == R.id.menu_task_edit_cancel) {
            presenter.cancelButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    public void showIconPickerDialog() {

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
        presenter.saveChildData(description);
    }

    @Override
    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            descriptionEditText.setText(description);
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
        finish();
    }
}
