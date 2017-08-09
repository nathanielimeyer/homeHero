package com.jbnm.homehero.ui.parent.taskList;

import com.jbnm.homehero.data.model.Task;

/**
 * Created by nathanielmeyer on 8/8/17.
 */

public interface ParentTaskClickListener {
    void onEditTask(String taskId);
    void onDeleteTask(Task task);
}
