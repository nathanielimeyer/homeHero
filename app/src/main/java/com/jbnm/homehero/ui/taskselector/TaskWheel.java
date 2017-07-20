package com.jbnm.homehero.ui.taskselector;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.jbnm.homehero.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskWheel extends View {
    private static final int MIN_ROTATION_TIME = 500;
    private static final int ROTATION_TIME = 250;

    private Paint textPaint;

    private int taskWheelColorOne;
    private int taskWheelColorTwo;
    private int textColor;
    private int textSize;

    private float centerX;
    private float centerY;
    private float radius;
    private float taskAngle;

    private RectF arc = new RectF();

    private List<TaskWheelItem> taskWheelItems = new ArrayList<>();

    private boolean animationRunning = false;
    private GestureDetector gestureDetector;
    private OnTaskSelectListener onTaskSelectListener;

    public TaskWheel(Context context) {
        super(context);
        init();
    }
    public TaskWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public TaskWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        gestureDetector = new GestureDetector(TaskWheel.this.getContext(), new GestureListener());
    }

    public void setTextColor(int color) {
        this.textColor = color;
        textPaint.setColor(textColor);
    }

    public void setTextSize(int size) {
        this.textSize = size;
        textPaint.setTextSize(textSize);
    }

    public void setTaskWheelColorOne(int color) {
        this.taskWheelColorOne = color;
    }

    public void setTaskWheelColorTwo(int color) {
        this.taskWheelColorTwo = color;
    }

    public void setOnTaskSelectListener(OnTaskSelectListener onTaskSelectListener) {
        this.onTaskSelectListener = onTaskSelectListener;
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
        super.onSizeChanged(w, h, oldw, oldh);
        float padX = (float)(getPaddingLeft() + getPaddingRight());
        float padY = (float)(getPaddingTop() + getPaddingBottom());
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w - padX, h - padY) / 2f;
        arc.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        calculateTextPaths();
    }

    private void calculateTextPaths() {
        for (TaskWheelItem item : this.taskWheelItems) {
            item.textPath = getTaskTextPath(item.startAngle);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (TaskWheelItem taskWheelItem : taskWheelItems) {
            canvas.drawArc(arc, taskWheelItem.startAngle, taskAngle, true, taskWheelItem.taskPaint);
            canvas.drawTextOnPath(taskWheelItem.task.getDescription(), taskWheelItem.textPath, 0f, 0f, textPaint);
        }
    }

    public void addTasks(List<Task> tasks) {
        taskAngle = 360f / tasks.size();
        for (int i = 0; i < tasks.size(); i++) {
            TaskWheelItem taskWheelItem = new TaskWheelItem();
            taskWheelItem.task = tasks.get(i);
            taskWheelItem.startAngle = taskAngle * i;
            taskWheelItem.taskPaint = getTaskPaint(taskWheelItem.task, i);
            taskWheelItems.add(taskWheelItem);
        }
        invalidate();
    }

    private Path getTaskTextPath(float startAngle) {
        double sectionCenterAngle = ((startAngle + (taskAngle / 2)) * (Math.PI / 180f));
        float x = (float)((radius * Math.cos(sectionCenterAngle)) + centerX);
        float y = (float)((radius * Math.sin(sectionCenterAngle)) + centerY);
        Path taskTextPath = new Path();
        taskTextPath.moveTo(centerX, centerY);
        taskTextPath.lineTo(x, y);
        return taskTextPath;
    }

    private Paint getTaskPaint(Task task, int index) {
        // TODO: improve color selection and add icon/text
        // TODO: draw differently if task is not available
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        if (index % 2 == 0) {
            paint.setColor(taskWheelColorOne);
        } else {
            paint.setColor(taskWheelColorTwo);
        }
        if (task.isAvailable()) {
            paint.setAlpha(255);
        } else {
            paint.setAlpha(150);
        }
        return paint;
    }

    private void rotateToTask(int targetTask, int rotations) {
        final int targetTaskIndex = getAvailableTask(targetTask);
        float currentRotation = getRotation();

        float angleToZero = currentRotation + (360 - currentRotation % 360);
        float angleToTask = 270 - (taskAngle * targetTaskIndex) - (taskAngle / 2);

        if (rotations > 6) rotations = 6;

        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(MIN_ROTATION_TIME + ROTATION_TIME * rotations)
                .rotation(angleToZero + (360 * rotations) + angleToTask)
                .setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {
                        animationRunning = true;
                    }
                    @Override public void onAnimationEnd(Animator animator) {
                        onTaskSelectListener.onTaskSelect(taskWheelItems.get(targetTaskIndex).task);
                        animationRunning = false;
                    }
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                }).start();
    }

    private void wrongDirection() {
        float currentAngle = getRotation();
        ObjectAnimator rotate = ObjectAnimator.ofFloat(TaskWheel.this,
                "rotation",
                currentAngle,
                currentAngle - 20f,
                currentAngle + 20f,
                currentAngle - 10f,
                currentAngle + 10f,
                currentAngle);
        rotate.setDuration(500);
        rotate.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {
                animationRunning = true;
            }
            @Override public void onAnimationEnd(Animator animator) {
                animationRunning = false;
            }
            @Override public void onAnimationCancel(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });
        rotate.start();
    }

    private int getAvailableTask(int taskIndex) {
        if (taskWheelItems.get(taskIndex).task.isAvailable()) {
            return taskIndex;
        }
        if (taskIndex == taskWheelItems.size() - 1) {
            taskIndex = 0;
        } else {
            taskIndex++;
        }
        return getAvailableTask(taskIndex);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) && !animationRunning;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float e1X = e1.getX() - centerX;
            float e1Y = e1.getY() - centerY;
            float e2X = e2.getX() - centerX;
            float e2Y = e2.getY() - centerY;

            if ((e1X * e2Y - e2X * e1Y) >= 0) {
                // clockwise if positive counter clockwise if negative
                float flingStrength = vectorToScalarScroll(velocityX, velocityY, e2X, e2Y);
                float angle = flingStrength % 360;
                int taskIndex = (int)Math.floor(Math.abs(angle) / taskAngle);
                int rotations = (int)Math.ceil(Math.abs(flingStrength) / 360);
                rotateToTask(taskIndex, rotations);
            } else {
                wrongDirection();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    // source: https://developer.android.com/training/custom-views/making-interactive.html
    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
        // get the length of the vector
        float l = (float) Math.sqrt(dx * dx + dy * dy);

        // decide if the scalar should be negative or positive by finding
        // the dot product of the vector perpendicular to (x,y).
        float crossX = -y;
        float crossY = x;

        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }

    private class TaskWheelItem {
        public Task task;
        public float startAngle;
        public Path textPath;
        public Paint taskPaint;
    }

    public interface OnTaskSelectListener {
        void onTaskSelect(Task task);
    }

}
