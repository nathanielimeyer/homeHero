package com.jbnm.homehero.ui.taskprogress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbnm.homehero.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by janek on 7/31/17.
 */

public class TaskInstructionsAdapter extends RecyclerView.Adapter<TaskInstructionsAdapter.TaskInstructionsViewHolder> {
    private List<String> instructions;

    public TaskInstructionsAdapter(List<String> instructions) {
        this.instructions = instructions;
    }


    @Override
    public TaskInstructionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_instruction_list_item, parent, false);
        TaskInstructionsViewHolder viewHolder = new TaskInstructionsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskInstructionsViewHolder holder, int position) {
        holder.bindStep(instructions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public class TaskInstructionsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stepNumberTextView) TextView stepNumberTextView;
        @BindView(R.id.stepDescriptionTextView) TextView stepDescriptionTextView;

        public TaskInstructionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindStep(String step, int position) {
            stepNumberTextView.setText(String.valueOf(position + 1));
            stepDescriptionTextView.setText(step);
        }

    }
}
