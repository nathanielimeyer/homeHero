package com.jbnm.homehero.ui.taskselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;

import java.util.List;

public class TaskSelector extends RelativeLayout {
    private View rootView;
    private TaskWheel taskWheel;

    private int textColor = Color.BLACK;
    private int textSize = getResources().getDimensionPixelSize(R.dimen.default_task_wheel_text_size);

    public TaskSelector(Context context) {
        super(context);
        init(context);
    }

    public TaskSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        init(context);
    }

    public TaskSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.task_selector, this);
        taskWheel = rootView.findViewById(R.id.taskSelectorWheel);
        taskWheel.setTextColor(textColor);
        taskWheel.setTextSize(textSize);
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TaskSelector, 0, 0);
        try {
            textColor = a.getColor(R.styleable.TaskSelector_taskTextColor, Color.BLACK);
            textSize = a.getDimensionPixelSize(R.styleable.TaskSelector_taskTextSize, getResources().getDimensionPixelSize(R.dimen.default_task_wheel_text_size));
        } finally {
            a.recycle();
        }
    }

    public void addTasks(List<Task> tasks) {
        taskWheel.addTasks(tasks);
    }

    public void setOnTaskSelectListener(TaskWheel.OnTaskSelectListener onTaskSelectListener) {
        taskWheel.setOnTaskSelectListener(onTaskSelectListener);
    }

    public void setOnSpinStartListener(TaskWheel.OnSpinStartListener onSpinStartListener) {
        taskWheel.setOnSpinStartListener(onSpinStartListener);
    }

}
