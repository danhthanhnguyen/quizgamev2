package com.thanhnguyen.quizgame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkStatus.getConnectivityStatus(context);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
