package com.jbnm.homehero.ui.parent.taskreview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

/**
 * Created by janek on 8/1/17.
 */

public class TaskReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> taskItems = new ArrayList<>();

    public TaskReviewAdapter(List<Object> taskItems) {
        this.taskItems = taskItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_review_header_item, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_review_list_item, parent, false);
            return new TaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == 0) {
            ((HeaderViewHolder) holder).bindHeader(taskItems.get(position).toString());
        } else {
            ((TaskViewHolder) holder).bindTask((Task) taskItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (taskItems.get(position) instanceof String) {
            return 0;
        } else {
            return 1;
        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.taskHeaderTextView) TextView taskHeaderTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHeader(String header) {
            taskHeaderTextView.setText(header);
        }
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.taskDescriptionTextView) TextView taskDescriptionTextView;
        @BindView(R.id.taskLastCompleteTextView) TextView taskLastCompleteTextView;
        @BindView(R.id.taskReviewIconImage) ImageView taskReviewIconImage;
        @BindView(R.id.taskApproveButton) ImageButton taskApproveButton;
        @BindView(R.id.taskRejectButton) ImageButton taskRejectButton;

        private Context context;

        public TaskViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bindTask(Task task) {
            taskDescriptionTextView.setText(task.getDescription());
            taskLastCompleteTextView.setText(String.format(context.getString(R.string.task_last_completed), getDate(task.getLastCompleted())));
//            taskReviewIconImage.setImageResource(getTaskIcon(task));
            if (task.pendingApproval()) {
                taskApproveButton.setVisibility(View.VISIBLE);
                taskRejectButton.setVisibility(View.VISIBLE);
            } else {
                taskApproveButton.setVisibility(View.GONE);
                taskRejectButton.setVisibility(View.GONE);
            }
        }

        private String getDate(long day) {
            // process date
            if (day == 0) {
                return "never";
            }
            return "never";
        }

        private int getTaskIcon(Task task) {
            if (task.getIcon() != null) {
                return context.getResources().getIdentifier(task.getIcon(), "drawable", context.getPackageName());
            } else {
                // TODO: add default task icon here
                return context.getResources().getIdentifier("down_arrow", "drawable", context.getPackageName());
            }
        }
    }
}