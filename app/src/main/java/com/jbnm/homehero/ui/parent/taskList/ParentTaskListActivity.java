package com.jbnm.homehero.ui.parent.taskList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.taskEdit.TaskEditorActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentTaskListActivity extends BaseActivity implements ParentTaskListContract.MvpView {

    private Context context = this;
    private ParentTaskListContract.Presenter presenter;

    public static Intent createIntent(Context context, String childId) {
        Intent intent = new Intent(context, ParentTaskListActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, childId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_task_list);
        ButterKnife.bind(this);

        String childId = getIntent().getStringExtra("childId");

        presenter = new ParentTaskListPresenter(this);
        presenter.loadChild(childId);


    }

    @OnClick(R.id.buttonEditTask)
    public void editTaskButton() {
        Intent intent = new Intent(ParentTaskListActivity.this, TaskEditorActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, Constants.CHILDID);
        startActivity(intent);
    }

    @Override
    public void setDescriptions(List<String> descriptions) {

    }

    @Override
    public void loadIcons(List<String> icons) {

    }
}
