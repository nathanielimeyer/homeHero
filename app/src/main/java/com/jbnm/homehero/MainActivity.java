package com.jbnm.homehero;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
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
    Reward reward = new Reward("1", "Disneyland", 15, "castle.jpg");
    List<Reward> rewards = Arrays.asList(reward);
    Child myChild = new Child("1", tasks, rewards, 16);
    // myChild.setTotalPoints(14);

    DecoView arcView = (DecoView)findViewById(R.id.dynamicArcView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (myChild.getTotalPoints() >= reward.getValue()) {
            rewardAnimation();
        } else {
            progressAnimation();
        }
    }


    private void progressAnimation() {
        // Create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(32f)
                .build());

        //Create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, 100, 0)
                .setLineWidth(32f)
                .build();

        int series1Index = arcView.addSeries(seriesItem1);

        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build());

        arcView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(4000).build());
        arcView.addEvent(new DecoEvent.Builder(100).setIndex(series1Index).setDelay(8000).build());
        arcView.addEvent(new DecoEvent.Builder(10).setIndex(series1Index).setDelay(12000).build());

        Log.d(TAG, "Show progress");
    }

    private void rewardAnimation() {
        Log.d(TAG, "Reward earned");

    }
}