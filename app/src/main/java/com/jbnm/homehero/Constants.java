package com.jbnm.homehero;

/**
 * Created by janek on 8/3/17.
 */

public class Constants {
    private Constants() {}

    public static final String TASK_INTENT_KEY = "taskId";
    public static final String TASK_NEW_INTENT_VALUE = "newTask";
    public static final String REWARD_NEW_INTENT_VALUE = "newReward";
    public static final String REWARD_INTENT_KEY = "rewardId";
    public static final String CHILD_INTENT_KEY = "childId";
    public static final int MIN_TASK_COUNT = 2;
    public static final int MAX_TASK_COUNT = 10;
    public static final int MIN_REWARD_COUNT = 2;
    public static final int MAX_REWARD_COUNT = 6;

    public static final String GOAL_PROGRESS_TASK_SELECT = "Get more points";
    public static final String GOAL_PROGRESS_TASK_PROGRESS = "Finish your task";

    public static final String TASK_PROGRESS_TITLE = "Task Progress";
    public static final String TASK_PICKER_TITLE = "Task Picker";
    public static final String GOAL_PROGRESS_TITLE = "Goal Progress";
    public static final String PARENT_TITLE = "Parent Interface";
    public static final String PARENT_TASK_LIST_TITLE = "Manage Tasks";
    public static final String PARENT_REWARD_LIST_TITLE = "Manage Rewards";
    public static final String PARENT_TASK_EDIT_TITLE = "Edit Task";
    public static final String PARENT_TASK_NEW_TITLE = "New Task";
    public static final String PARENT_REWARD_EDIT_TITLE = "Edit Reward";
    public static final String PARENT_REWARD_NEW_TITLE = "New Reward";

    public static final String DEFAULT_CHORE_ICON = "chore_broom";
    public static final String DEFAULT_REWARD_ICON = "goal_dice";
    // Temporary until auth is set up
    // used in base activity and main activity
    public static final String CHILDID = "-KqZYJOtnw-96-kxmWZC";
}
