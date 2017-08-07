package com.jbnm.homehero.ui.taskEdit;

import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jbnm.homehero.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskEditorInstructionsAdapter extends RecyclerView.Adapter<TaskEditorInstructionsAdapter.TaskEditorInstructionsViewHolder> {
    private static final String TAG = "Task Editor Adapter";
    private List<String> instructions;

    public TaskEditorInstructionsAdapter(List<String> instructions) {
        this.instructions = instructions;
    }

    @Override
    public TaskEditorInstructionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_editor_instruction_list_item, parent, false);
        TaskEditorInstructionsViewHolder viewHolder = new TaskEditorInstructionsViewHolder(
                view,
                new MyCustomEditTextListener(),
                new DeleteButtonOnClickListener());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskEditorInstructionsViewHolder holder, int position) {
        holder.bindStep(instructions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public class TaskEditorInstructionsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stepNumberTextView) TextView stepNumberTextView;
        @BindView(R.id.stepDescriptionEditText) EditText stepDescriptionEditText;
        @BindView(R.id.stepDeleteButton) Button stepDeleteButton;
        public MyCustomEditTextListener myCustomEditTextListener;
        public DeleteButtonOnClickListener deleteButtonOnClickListener;

        public TaskEditorInstructionsViewHolder(View itemView,
                                                MyCustomEditTextListener myCustomEditTextListener,
                                                DeleteButtonOnClickListener deleteButtonOnClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.stepDescriptionEditText = (EditText) itemView.findViewById(R.id.stepDescriptionEditText);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.stepDescriptionEditText.addTextChangedListener(myCustomEditTextListener);

            this.stepDeleteButton = (Button) itemView.findViewById(R.id.stepDeleteButton);
            this.deleteButtonOnClickListener = deleteButtonOnClickListener;
            this.stepDeleteButton.setOnClickListener(deleteButtonOnClickListener);

        }

        public void bindStep(String step, int position) {
            stepNumberTextView.setText(String.valueOf(position + 1));
            myCustomEditTextListener.updatePosition(position);
            if (step != null && !step.isEmpty() && !step.equals("")) {
                stepDescriptionEditText.setText(step);
            } else {
                stepDescriptionEditText.setHint("Tap to edit");
            }
            deleteButtonOnClickListener.updatePosition(position);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            instructions.set(position, charSequence.toString());
            Log.d(TAG, "Position = " + position + charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class DeleteButtonOnClickListener implements View.OnClickListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            instructions.remove(position);
            notifyDataSetChanged();
        }
    }
}
