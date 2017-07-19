package com.jbnm.homehero.ui.taskwheel;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private Paint textPaint;
    private int backgroundColor, taskWheelColorOne, taskWheelColorTwo;

    private float centerX, centerY, radius, taskAngle;
    private RectF arc = new RectF();

    private List<Task> tasks = new ArrayList<>();
    private GestureDetector gestureDetector;
    private OnTaskSelectListener onTaskSelectListener;

    public interface OnTaskSelectListener {
        void onTaskSelect(Task task);
    }

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
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(42f);

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
        for (Task task : tasks) {
            drawItem(canvas, task);
        }
    }

    private void drawItem(Canvas canvas, Task task) {
        int taskIndex = tasks.indexOf(task);
        float startAngle = taskIndex * taskAngle;
        // TODO: improve color selection and add icon/text
        // TODO: draw differently if task is not available

        if (taskIndex % 2 == 0) {
            taskPaint.setColor(taskWheelColorOne);
        } else {
            taskPaint.setColor(taskWheelColorTwo);
        }
        canvas.drawArc(arc, startAngle, taskAngle, true, taskPaint);

        // draw task description in middle of section
        double sectionCenterAngle = ((startAngle + (taskAngle / 2)) * (Math.PI / 180f));
        float x = (float)((radius * Math.cos(sectionCenterAngle)) + centerX);
        float y = (float)((radius * Math.sin(sectionCenterAngle)) + centerY);
        Path testPath = new Path();
        testPath.moveTo(centerX, centerY);
        testPath.lineTo(x, y);
        canvas.drawTextOnPath(task.getDescription(), testPath, radius - 200f, 0f, textPaint);

    }

    public void addTasks(List<Task> tasks) {
        this.tasks = tasks;
        taskAngle = 360f / this.tasks.size();
        invalidate();
    }

    public void setOnTaskSelectListener(OnTaskSelectListener onTaskSelectListener) {
        this.onTaskSelectListener = onTaskSelectListener;
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
                        onTaskSelectListener.onTaskSelect(tasks.get(targetTaskIndex));
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
        if (tasks.get(taskIndex).isAvailable()) {
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
                // TODO: prevent additional touch events
            } else {
                // TODO: add animation for indicate wrong direction
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

}
