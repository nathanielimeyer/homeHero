package com.jbnm.homehero.ui.taskpicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goalProgressIntent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun taskProgressIntent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading(): Boolean {
        return false
    }

    override fun hideLoading() {
    }

    override fun showError(): Boolean {
        return false
    }

    override fun hideError() {
    }
}
