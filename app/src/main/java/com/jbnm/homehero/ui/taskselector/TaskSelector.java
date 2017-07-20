package com.jbnm.homehero.ui.taskselector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;

import java.util.List;

public class TaskSelector extends RelativeLayout {
    private View rootView;
    private TaskWheel taskWheel;

    public TaskSelector(Context context) {
        super(context);
        init(context);
    }

    public TaskSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TaskSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.task_selector, this);
        taskWheel = rootView.findViewById(R.id.taskSelectorWheel);
    }

    public void addTasks(List<Task> tasks) {
        taskWheel.addTasks(tasks);
    }

    public void setOnTaskSelectListener(TaskWheel.OnTaskSelectListener onTaskSelectListener) {
        taskWheel.setOnTaskSelectListener(onTaskSelectListener);
    }

}
