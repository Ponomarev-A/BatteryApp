package com.example.user15.batteryapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ImageView mBatteryView;
    private TextView mBatteryPercentageView;
    private TextView mBatteryTechnology;
    private TextView mBatteryStatus;

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int maxCapacity = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int currCapacity = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

            int batteryPercentage = Math.round(currCapacity * 100.0f / maxCapacity);

            mBatteryView.getDrawable().setLevel(batteryPercentage * 100);
            mBatteryPercentageView.setText(getString(R.string.battery_percent_format, batteryPercentage));
            mBatteryTechnology.setText(technology);
            mBatteryStatus.setText(getResources().getStringArray(R.array.battery_states)[getBatteryState(intent)]);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBatteryView = (ImageView) findViewById(R.id.iv_battery);
        mBatteryPercentageView = (TextView) findViewById(R.id.tv_battery_percentage);
        mBatteryTechnology = (TextView) findViewById(R.id.tv_battery_technology);
        mBatteryStatus = (TextView) findViewById(R.id.tv_battery_status);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mBatteryReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isBatteryPresent(Intent intent) {
        return intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, true);
    }

    private int getBatteryState(Intent intent) {
        int state = 0;
        if (isBatteryPresent(intent)) {
            state = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN);
        }

        return state;
    }
}
