package com.jbnm.homehero.ui.taskpicker

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jbnm.homehero.R
import com.jbnm.homehero.data.model.Task

import kotlinx.android.synthetic.main.activity_task_picker.*

class TaskPickerActivity : AppCompatActivity(), TaskPickerContract.MvpView {

    lateinit var presenter: TaskPickerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_picker)
        presenter = TaskPickerPresenter(this)
        presenter.loadTasks()

        taskSelector.setOnTaskSelectListener { presenter.taskSelected(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun addTasks(tasks: List<Task>) {
        taskSelector.addTasks(tasks)
    }

    override fun showSelectedTask(task: String) {
        Toast.makeText(this, task, Toast.LENGTH_SHORT).show()
    }

    override fun showTasksCompleted(tasksCompleted: Int, tasksRequired: Int) {
        tasksCompletedTextView.text = String.format(getString(R.string.tasks_completed), tasksCompleted, tasksRequired)
        tasksCompletedRatingBar.numStars = tasksRequired
        val animation: ObjectAnimator = ObjectAnimator.ofFloat(tasksCompletedRatingBar, "rating", tasksCompleted.toFloat())
        animation.duration = 500
        animation.start()

    }

    override fun goalProgressIntent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun taskProgressIntent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading(): Boolean {
        taskSelector.visibility = View.GONE
        tasksCompletedTextView.visibility = View.GONE
        tasksCompletedRatingBar.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        return false
    }

    override fun hideLoading() {
        taskSelector.visibility = View.VISIBLE
        tasksCompletedTextView.visibility = View.VISIBLE
        tasksCompletedRatingBar.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun showError(): Boolean {
        return false
    }

    override fun hideError() {
    }
}
