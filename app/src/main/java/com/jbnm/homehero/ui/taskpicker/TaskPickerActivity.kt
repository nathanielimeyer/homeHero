package com.jbnm.homehero.ui.taskpicker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.jbnm.homehero.R
import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.ui.goal.GoalActivity

import kotlinx.android.synthetic.main.activity_task_picker.*

class TaskPickerActivity : AppCompatActivity(), TaskPickerContract.MvpView {

    lateinit var presenter: TaskPickerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_picker)

        presenter = TaskPickerPresenter(this)
//        presenter.loadTasks()

        taskSelector.setOnTaskSelectListener { presenter.taskSelected(it) }

        goalProgressButton.setOnClickListener { presenter.goalButtonClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun addTasks(tasks: List<Task>) {
        taskSelector.addTasks(tasks)
    }

    override fun showSelectedTask(task: Task) {
        val animationDuration = resources.getInteger(android.R.integer.config_longAnimTime).toLong()

        resultTextView.text = String.format(getString(R.string.task_select_result), task.description)
        taskProgressButton.setOnClickListener { presenter.taskButtonClick(task) }

        result.alpha = 0f
        result.visibility = View.VISIBLE

        result.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null)

        content.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        content.visibility = View.GONE
                    }
                })

    }

    override fun showTasksCompleted(tasksCompleted: Int, tasksRequired: Int) {
        tasksCompletedTextView.text = String.format(getString(R.string.tasks_completed), tasksCompleted, tasksRequired)
        tasksCompletedRatingBar.numStars = tasksRequired
        val animation: ObjectAnimator = ObjectAnimator.ofFloat(tasksCompletedRatingBar, "rating", tasksCompleted.toFloat())
        animation.duration = 500
        animation.startDelay = 500
        animation.start()

    }

    override fun goalProgressIntent() {
        val intent: Intent = Intent(this, GoalActivity::class.java)
        startActivity(intent)
    }

    override fun taskProgressIntent(task: Task) {
        Log.d("TaskPickerActivity", task.description)
    }

    override fun showLoading(): Boolean {
        result.visibility = View.GONE
        content.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        return false
    }

    override fun hideLoading() {
        val animationDuration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

        content.alpha = 0f
        content.visibility = View.VISIBLE

        content.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null)

        progressBar.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressBar.visibility = View.GONE
                    }
                })
    }

    override fun showError(): Boolean {
        return false
    }

    override fun hideError() {
    }
}
