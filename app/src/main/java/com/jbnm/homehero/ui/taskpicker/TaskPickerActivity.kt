package com.jbnm.homehero.ui.taskpicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jbnm.homehero.R
import com.jbnm.homehero.data.model.Task

import kotlinx.android.synthetic.main.activity_task_picker.*

class TaskPickerActivity : AppCompatActivity(), TaskPickerMvpView {

    lateinit var presenter: TaskPickerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_picker)
        presenter = TaskPickerPresenter(this)
        presenter.loadTasks()

        taskSelector.setOnTaskSelectListener { task -> Toast.makeText(this, task.description, Toast.LENGTH_SHORT).show() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun addTasks(tasks: List<Task>) {
        taskSelector.addTasks(tasks)
    }
}
