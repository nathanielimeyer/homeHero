package com.jbnm.homehero.ui.goal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.parent.ParentActivity;
import com.jbnm.homehero.ui.taskpicker.TaskPickerActivity;
import com.jbnm.homehero.ui.taskprogress.TaskProgressActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jbnm.homehero.R.array.replace_me;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalActivity extends BaseActivity implements GoalContract.MvpView {
    @BindView(R.id.dynamicArcView) DecoView arcView;
    @BindView(R.id.imageView) ImageView rewardImageView;
    @BindView(R.id.taskButton) Button taskButton;
    @BindView(R.id.goalDescriptionTextView) TextView goalDescriptionTextView;
    public GoalPresenter presenter;
    private Context context = this;
    ListAdapter adapter;

    public static Intent createIntent(Context context, String childId) {
        Intent intent = new Intent(context, GoalActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, childId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        ButterKnife.bind(this);

        String childId = getIntent().getStringExtra(Constants.CHILD_INTENT_KEY);

        presenter = new GoalPresenter(this);
        presenter.loadData(childId);

        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.taskButtonClicked();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.child, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_child_parentNav_item) {
            presenter.handleParentNavButtonClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void buildGoalPickerDialog() {
        List<GoalPresenter.DialogItem> dialogItemList = presenter.buildRewardDialogList();
        final GoalPresenter.DialogItem[] dialogItems = new GoalPresenter.DialogItem[dialogItemList.size()];
        dialogItemList.toArray(dialogItems);

        adapter = new ArrayAdapter<GoalPresenter.DialogItem>(
                this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                dialogItems){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                //Put the image on the TextView

                tv.setCompoundDrawablesWithIntrinsicBounds(dialogItems[position].image, 0, 0, 0);

//                Change dialogItems image type to String in presenter if you use this
//                tv.setCompoundDrawablesWithIntrinsicBounds((getResources().getIdentifier(dialogItems[position].image, "drawable", getPackageName())), 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };
    }

    @Override
    public void showProgress(String description, int rewardValue, String rewardImage, int approvedPoints, int pendingPoints) {
        arcView.executeReset();
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255,218,218,218))
                .setRange(0, rewardValue, rewardValue)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .setLineWidth(32f)
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 196, 64, 0))
                .setRange(0, rewardValue, 0)
                .setInitialVisibility(false)
                .setLineWidth(46f)
//                .setSeriesLabel(new SeriesLabel.Builder("Pending").build())
                .setCapRounded(false)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.3f))
                .setShowPointWhenEmpty(false)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, rewardValue, 0)
                .setInitialVisibility(false)
                .setLineWidth(46f)
//                .setSeriesLabel(new SeriesLabel.Builder("Approved").build())
                .setCapRounded(false)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.3f))
                .setShowPointWhenEmpty(false)
                .build();

        int series1Index = arcView.addSeries(seriesItem1);
        int series2Index = arcView.addSeries(seriesItem2);

        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(30000)
                .setDuration(2000)
                .build());

        arcView.addEvent(new DecoEvent.Builder(approvedPoints)
                .setIndex(series2Index)
                .setDuration(1250)
                .build());

        arcView.addEvent(new DecoEvent.Builder(approvedPoints + pendingPoints)
                .setIndex(series1Index)
                .setDuration(1500)
                .build());
    }

    @Override
    public void showRewardAnimation() {
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.heart_pulse);
        pulse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                presenter.rewardAnimationEnded();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        rewardImageView.startAnimation(pulse);
    }

    @Override
    public void showGoalPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.goal_picker_title)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.setNewRewardAndDecrementPoints(i);
                    }
                });
        builder.create().show();
    }

    @Override
    public void hideTaskButton() {
        taskButton.setVisibility(View.GONE);
    }

    @Override
    public void showTaskButton() {
        taskButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void taskPickerIntent(String childId) {
        startActivity(TaskPickerActivity.createIntent(this, childId));
    }

    @Override
    public void taskProgressIntent(String childId) {
        startActivity(TaskProgressActivity.createIntent(this, childId));
    }

    @Override
    public void parentIntent(String childId) {
        startActivity(ParentActivity.createIntent(this, childId));
    }

    @Override
    public void setGoalDescription(String description) {
        goalDescriptionTextView.setText(description);
    }

    @Override
    public void setGoalImage(String rewardImage) {
        rewardImageView.setImageResource(getResources().getIdentifier(rewardImage, "drawable", getPackageName()));
    }
}
