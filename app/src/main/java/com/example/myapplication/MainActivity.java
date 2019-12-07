package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.a7alma9.wagonwheel.WagonWheelView;
import com.a7alma9.wagonwheel.WheelData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    WagonWheelView wagonWheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wagonWheelView = findViewById(R.id.wagonWheel);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wagonWheelView.setData(constructWagonWheelData(wagonWheelView.getRadius()));
                wagonWheelView.invalidate();
            }
        }, 1000);
    }

    private ArrayList<WheelData> constructWagonWheelData(int radius) {

        ArrayList<WheelData> wagonWheelData = new ArrayList<>();

        for (int x = 0; x < 30; x++) {

            wagonWheelData.add(new WheelData(((int) (Math.random() * 360 + 1)),
                    ((int) (Math.random() * radius + 1)),
                    ((int) (Math.random() * 6 + 1)),
                    ((int) (Math.random() * 5 + 1))));
        }

        return wagonWheelData;

    }
}
