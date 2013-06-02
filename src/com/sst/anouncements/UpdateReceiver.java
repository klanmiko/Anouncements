package com.sst.anouncements;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sst.anouncements.dummy.DummyContent;

import java.io.IOException;

/**
 * Created by eternitysst on 5/29/13.
 */
public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent intent1 = new Intent(context, UpdateService.class);

        context.startService(intent1);
    }
}
