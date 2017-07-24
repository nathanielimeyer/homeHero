package com.jbnm.homehero.ui.goal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Child;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nathanielmeyer on 7/18/17.
 */

public class GoalActivity extends AppCompatActivity implements GoalContract.MvpView {
    @BindView(R.id.dynamicArcView) DecoView arcView;
    @BindView(R.id.imageView)
    ImageView rewardImageView;
    GoalPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        ButterKnife.bind(this);
//        presenter = new GoalPresenter(this);
        showProgress("Disneyland", 20, "sadfasdf", 10, 5);
    }


    @Override
    public void showProgress(String description, int rewardValue, String rewardImage, int approvedPoints, int pendingPoints) {

        arcView.addSeries(new SeriesItem.Builder(Color.argb(255,218,218,218))
                .setRange(0, rewardValue, rewardValue)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .setLineWidth(32f)
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 196, 64, 0))
                .setRange(0, rewardValue, 0)
                .setInitialVisibility(false)
                .setLineWidth(46f)
                .setSeriesLabel(new SeriesLabel.Builder("Pending").build())
                .setCapRounded(false)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.3f))
                .setShowPointWhenEmpty(false)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, rewardValue, 0)
                .setInitialVisibility(false)
                .setLineWidth(46f)
                .setSeriesLabel(new SeriesLabel.Builder("Approved").build())
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
//                .setDelay(30000)
                .build());

        arcView.addEvent(new DecoEvent.Builder(approvedPoints + pendingPoints)
                .setIndex(series1Index)
                .setDuration(1500)
//                .setDelay(30000)
                .build());
    }


    @Override
    public boolean showLoading() {
        return false;
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public boolean showError() {
        return false;
    }


    @Override
    public void hideError() {

    }

    @Override
    public void showRewardAnimation() {

    }
}
