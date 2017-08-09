package com.jbnm.homehero.ui.parent.taskList;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nathanielmeyer on 8/8/17.
 */

class ParentTaskListAdapter extends RecyclerView.Adapter<ParentTaskListAdapter.ParentTaskListViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private ParentTaskClickListener parentTaskClickListener;
    private Context context;

    public ParentTaskListAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public ParentTaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_task_list_item, parent, false);
        ParentTaskListViewHolder viewHolder = new ParentTaskListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParentTaskListViewHolder holder, int position) {
        holder.bindTask(tasks.get(position), position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setParentTaskClickListener(ParentTaskClickListener parentTaskClickListener) {
        this.parentTaskClickListener = parentTaskClickListener;
    }

    public class ParentTaskListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.taskImage)
        ImageView taskImage;
        @BindView(R.id.taskDescriptionTextView)
        TextView taskDescriptionTextView;
        @BindView(R.id.taskEditButton)
        ImageButton taskEditButton;
        @BindView(R.id.taskDeleteButton)
        ImageButton taskDeleteButton;

        private Task task;


        public ParentTaskListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            ButterKnife.bind(this, itemView);
        }

        public void bindTask(Task task, int position) {
            this.task = task;
            if (task.getIcon() != null && !task.getIcon().equals("")) {
                taskImage.setImageResource(context.getResources().getIdentifier(task.getIcon(), "drawable", context.getPackageName()));
            }
            taskDescriptionTextView.setText(task.getDescription());
            taskEditButton.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.taskEditButton)
        public void taskEditClick() {
            parentTaskClickListener.onEditTask(task.getId());
        }

        @OnClick(R.id.taskDeleteButton)
        public void taskDeleteClick() {
            parentTaskClickListener.onDeleteTask(task);
        }
    }
}