package com.jbnm.homehero.ui.taskwheel;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskWheel extends View {
    private final int MIN_ROTATION_TIME = 500;
    private final int ROTATION_TIME = 250;
    private Paint circlePaint;
    private Paint taskPaint;
    private int backgroundColor, taskWheelColorOne, taskWheelColorTwo;

    private float centerX, centerY, radius, taskAngle;
    private RectF arc = new RectF();

    private List<Task> taskItems = new ArrayList<>();

    private GestureDetector gestureDetector;

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

        gestureDetector = new GestureDetector(TaskWheel.this.getContext(), new GestureListener());
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
        float padX = (float)(getPaddingLeft() + getPaddingRight());
        float padY = (float)(getPaddingTop() + getPaddingBottom());
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w - padX, h - padY) / 2f;
        arc.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
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
        if (taskIndex == 0) {
            taskPaint.setColor(Color.BLACK);
        } else if (taskIndex % 2 == 0) {
            taskPaint.setColor(taskWheelColorOne);
        } else {
            taskPaint.setColor(taskWheelColorTwo);
        }
        canvas.drawArc(arc, startAngle, taskAngle, true, taskPaint);
    }

    public void addTasks(List<Task> tasks) {
        taskItems = tasks;
        taskAngle = 360f / taskItems.size();
        invalidate();
    }

    private void rotateToTask(int targetTask, int rotations) {
        resetWheel();
        final int targetTaskIndex = getAvailableTask(targetTask);
        float targetItemAngle = taskAngle * targetTaskIndex;
        float targetAngle = 270 - targetItemAngle - (taskAngle / 2);

        if (rotations > 6) rotations = 6;

        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(MIN_ROTATION_TIME + ROTATION_TIME * rotations)
                .rotation((360 * rotations) + targetAngle)
                .setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override public void onAnimationEnd(Animator animator) {
                        Log.d("test", "Animation finished. Landed on " + taskItems.get(targetTaskIndex).getDescription());
                        clearAnimation();
                    }
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                }).start();
    }

    private void resetWheel() {
        animate().setDuration(0)
                .rotation(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override public void onAnimationEnd(Animator animator) {
                        clearAnimation();
                    }
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                }).start();
    }

    private int getAvailableTask(int taskIndex) {
        if (taskItems.get(taskIndex).isAvailable()) {
            return taskIndex;
        }
        taskIndex++;
        return getAvailableTask(taskIndex);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = gestureDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                result = true;
            }
        }
        return result;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float flingStrength = vectorToScalarScroll(velocityX, velocityY, e2.getX() - centerX, e2.getY() - centerY);
            float angle = flingStrength % 360;
            int taskIndex = (int)Math.floor(Math.abs(angle) / taskAngle);
            int rotations = (int)Math.ceil(Math.abs(flingStrength) / 360);
            // TODO: Add check to make sure rotation is only clockwise
            rotateToTask(taskIndex, rotations);
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

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

}
