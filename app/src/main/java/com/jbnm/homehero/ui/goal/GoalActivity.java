package com.jbnm.homehero.ui.goal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hookedonplay.decoviewlib.DecoView;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Child;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalActivity extends AppCompatActivity implements GoalMvpView {

    GoalPresenter presenter;

    DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);
    Child myChild = new Child();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        presenter = new GoalPresenter(this);

    }


    @Override
    public void showProgress(String id, String description, int value, String rewardImage, int approvedPoints, int pendingPoints) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }
}
