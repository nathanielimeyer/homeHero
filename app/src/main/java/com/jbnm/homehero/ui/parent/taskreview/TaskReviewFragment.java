package com.jbnm.homehero.ui.parent.taskreview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jbnm.homehero.R;

public class TaskReviewFragment extends Fragment implements TaskReviewContract.MvpView {
    private TaskReviewContract.Presenter presenter;

    public TaskReviewFragment() {
        // Required empty public constructor
    }

    public static TaskReviewFragment newInstance() {
        TaskReviewFragment fragment = new TaskReviewFragment();
        // add to bundle here
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_review, container, false);
    }

}
