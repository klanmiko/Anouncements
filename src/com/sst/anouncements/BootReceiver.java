package com.sst.anouncements;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by eternitysst on 5/29/13.
 */
public class BootReceiver extends BroadcastReceiver {
    private boolean running = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!running) {

            AlarmManager service = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, UpdateIntentService.class);
            PendingIntent pending = PendingIntent.getService(context, 0, i,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            //starts update service 3 mins after boot, then loads frequency from settings file
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 3);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String syncConnPref = sharedPref.getString("pref_key_refresh", "0");
            Integer no;
            if (syncConnPref.equalsIgnoreCase("Manually")) {
                no = 0;
                service.cancel(pending);

            } else if (syncConnPref.equalsIgnoreCase("15 Mins")) {
                service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pending);
            } else if (syncConnPref.equalsIgnoreCase("30 Mins")) {
                service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pending);
            } else if (syncConnPref.equalsIgnoreCase("Every Hour")) {
                service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pending);
            } else if (syncConnPref.equalsIgnoreCase("Every Day")) {
                service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending);
            } else {
                no = new Integer(syncConnPref);
                service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        cal.getTimeInMillis(), 1000 * 60 * no, pending);
            }


            //
            // Fetch every 30 seconds
            // InexactRepeating allows Android to optimize the energy consumption

            running = true;
        }

    }
}
