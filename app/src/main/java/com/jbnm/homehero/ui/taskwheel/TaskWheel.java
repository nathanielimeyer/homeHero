package com.jbnm.homehero.ui.taskwheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskWheel extends View {
    private Paint circlePaint;
    private Paint taskPaint;
    private int backgroundColor, taskWheelColorOne, taskWheelColorTwo;

    private float centerX, centerY, radius, taskAngle;
    private RectF arc = new RectF();

    private List<Task> taskItems = new ArrayList<>();

    public TaskWheel(Context context) {
        this(context, null);
    }

    public TaskWheel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaskWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        backgroundColor = ContextCompat.getColor(context, R.color.taskWheelBackground);
        taskWheelColorOne = ContextCompat.getColor(context, R.color.taskWheelColorOne);
        taskWheelColorTwo = ContextCompat.getColor(context, R.color.taskWheelColorTwo);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(backgroundColor);
        taskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        taskPaint.setStyle(Paint.Style.FILL);
        taskPaint.setColor(taskWheelColorOne);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float padX = (float)(getPaddingLeft() - getPaddingRight());
        float padY = (float)(getPaddingTop() - getPaddingBottom());
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w - padX, h - padY) / 2f;
        arc.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
    }

    public void addTasks(List<Task> tasks) {
        taskItems = tasks;
        taskAngle = 360 / taskItems.size();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        for (Task task : taskItems) {
            drawItem(canvas, task);
        }
    }

    private void drawItem(Canvas canvas, Task task) {
        int taskIndex = taskItems.indexOf(task);
        float startAngle = taskIndex * taskAngle;
        if (taskIndex % 2 == 0) {
            taskPaint.setColor(taskWheelColorOne);
        } else {
            taskPaint.setColor(taskWheelColorTwo);
        }
        canvas.drawArc(arc, startAngle, taskAngle, true, taskPaint);
    }
}
