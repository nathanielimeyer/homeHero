package com.jbnm.homehero;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;

import java.util.Arrays;
import java.util.List;

class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    List<String> instructions = Arrays.asList("change bed", "vacuum walls");
    Task myTask = new Task("1", "Wash your room", instructions, true );
    List<Task> tasks = Arrays.asList(myTask);
    Reward reward = new Reward("1", "Disneyland", 20, "castle.jpg");
    List<Reward> rewards = Arrays.asList(reward);
    Child myChild = new Child("1", tasks, rewards);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        myChild.setTotalPoints(10);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);

        arcView.addSeries(new SeriesItem.Builder(Color.argb(255,218,218,218))
                .setRange(0, reward.getValue(), reward.getValue())
                .setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .setLineWidth(32f)
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 196, 64, 0))
                .setRange(0, reward.getValue(), 0)
                .setInitialVisibility(false)
                .setLineWidth(46f)
                .setSeriesLabel(new SeriesLabel.Builder("Pending").build())
                .setCapRounded(false)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, Color.parseColor("#22000000"), 0.3f))
                .setShowPointWhenEmpty(false)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, reward.getValue(), 0)
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

        arcView.addEvent(new DecoEvent.Builder(myChild.getTotalPoints())
                .setIndex(series2Index)
                .setDuration(1250)
//                .setDelay(30000)
                .build());

        arcView.addEvent(new DecoEvent.Builder(myChild.getTotalPoints() + myChild.calculatePendingPoints())
                .setIndex(series1Index)
                .setDuration(1500)
//                .setDelay(30000)
                .build());

        if (myChild.getTotalPoints() >= reward.getValue()) {
            rewardAnimation();
        } else {
            progressAnimation();
        }
    }


    private void progressAnimation() {
        Log.d(TAG, "Show progress");
    }

    private void rewardAnimation() {
        Log.d(TAG, "Reward earned");
    }
}