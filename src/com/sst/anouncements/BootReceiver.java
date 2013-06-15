package com.sst.anouncements;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by eternitysst on 5/29/13.
 */
public class BootReceiver extends BroadcastReceiver {
    boolean running = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!running) {

            AlarmManager service = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, UpdateReceiver.class);
            PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar cal = Calendar.getInstance();
            // Start 30 seconds after boot completed

            cal.add(Calendar.MINUTE, 3);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String syncConnPref = sharedPref.getString("pref_key_refresh", "0");
            Integer no;
            if (syncConnPref.equalsIgnoreCase("Manually")) {
                no = 0;

            } else {
                no = new Integer(syncConnPref);
            }
            //
            // Fetch every 30 seconds
            // InexactRepeating allows Android to optimize the energy consumption
            service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), 1000 * 60 * no, pending);
            running = true;
        }

    }
}
