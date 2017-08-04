package com.jbnm.homehero.ui.taskpicker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.jbnm.homehero.Constants
import com.jbnm.homehero.R
import com.jbnm.homehero.data.model.Task
import com.jbnm.homehero.ui.base.BaseActivity
import com.jbnm.homehero.ui.goal.GoalActivity
import com.jbnm.homehero.ui.taskprogress.TaskProgressActivity

import kotlinx.android.synthetic.main.activity_task_picker.*
import kotlinx.android.synthetic.main.task_picker_result.*

class TaskPickerActivity : BaseActivity(), TaskPickerContract.MvpView {

    companion object {
        @JvmStatic fun createIntent(context: Context, childId: String) : Intent {
            val intent: Intent = Intent(context, TaskPickerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.putExtra(Constants.CHILD_INTENT_KEY, childId)
            return intent
        }
    }

    lateinit var presenter: TaskPickerContract.Presenter
    lateinit var tutorialAnimation: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_picker)

        val childId: String = intent.getStringExtra(Constants.CHILD_INTENT_KEY)

        presenter = TaskPickerPresenter(this, childId)
        result.visibility = View.GONE

        taskSelector.setOnTaskSelectListener { presenter.taskSelected(it) }
        goalProgressButton.setOnClickListener { presenter.handleGoalButtonClick() }
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
        taskProgressButton.setOnClickListener { presenter.handleTaskButtonClick() }

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

    override fun showTasksCompleted(tasksCompleted: Int) {
        tasksCompletedTextView.text = String.format(getString(R.string.tasks_completed), tasksCompleted)
    }

    override fun goalProgressIntent(childId: String) {
        startActivity(GoalActivity.createIntent(this, childId));
    }

    override fun taskProgressIntent(childId: String) {
        startActivity(TaskProgressActivity.createIntent(this, childId))
    }

    override fun showTutorial() {
        tutorialImageView.visibility = View.VISIBLE
        tutorialAnimation = ObjectAnimator.ofPropertyValuesHolder(tutorialImageView,
                PropertyValuesHolder.ofFloat("scaleX", 0.8f),
                PropertyValuesHolder.ofFloat("scaleY", 0.8f))
        tutorialAnimation.duration = 500
        tutorialAnimation.repeatCount = ObjectAnimator.INFINITE
        tutorialAnimation.repeatMode = ObjectAnimator.REVERSE
        tutorialAnimation.start()

        taskSelector.setOnSpinStartListener { presenter.handleTutorialClick() }
    }

    override fun hideTutorial() {
        tutorialImageView.visibility = View.GONE
        tutorialAnimation.end()
    }
}
