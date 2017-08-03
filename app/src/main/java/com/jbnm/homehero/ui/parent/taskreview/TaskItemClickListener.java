package com.jbnm.homehero.ui.parent.taskreview;

/**
 * Created by janek on 8/1/17.
 */

public interface TaskItemClickListener {
    void onTaskItemApprove(String taskId);
    void onTaskItemReject(String taskId);
}
