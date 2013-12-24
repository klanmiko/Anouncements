package com.sst.anouncements;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.sst.anouncements.dummy.DummyContent;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by eternitysst on 5/25/13.
 */
public class UpdateService extends Service {

    private boolean bound = false;
    private final IBinder bind = new ActiveBind();
    private PowerManager.WakeLock wakeLock;
    FeedParse feedParse = new FeedParse();

    private final BroadcastReceiver updatereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String number = intent.getStringExtra("new");
            Integer n = Integer.valueOf(number);
            int pos = 0;
            if (intent.hasExtra("showfield")) {
                pos = DummyContent.findByAuthor(intent.getStringExtra("showfield"));
            }

            if (n > 0) {

                Notificate(n, pos);
            } else {

                try {
                    wakeLock.release();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            stopSelf();


        }
    };

    public UpdateService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction("com.sst.announcements.UPDATED");
        this.registerReceiver(updatereceiver, theFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(updatereceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        bound = true;
        return bind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bound = false;
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            DummyContent.load(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        PowerManager mgr = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
        String URL = "http://studentsblog.sst.edu.sg//feeds/posts/default";

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfor = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assert networkInfo != null;
        boolean wifiConnected = networkInfo.isConnected();
        boolean mobileConnected;
        if (mobileInfor != null) {
            mobileConnected = mobileInfor.isConnected();
        } else {
            mobileConnected = false;
        }
        DummyContent.DateSave save = null;
        try {
            save = DummyContent.loadDate(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (save != null) {
            GregorianCalendar cal = new GregorianCalendar(save.year, save.month, save.day);
            Calendar calnow = GregorianCalendar.getInstance();
            if (calnow.after(cal)) {
                DummyContent.invalidate(this.getApplicationContext());
                try {
                    DummyContent.load(this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                cal.add(GregorianCalendar.MONTH, 1);
                save = new DummyContent.DateSave(cal.get(GregorianCalendar.DAY_OF_MONTH), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.YEAR));
                try {
                    DummyContent.saveDate(save, this.getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } else {
            DummyContent.invalidate(this.getApplicationContext());
            try {
                DummyContent.load(this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            save = new DummyContent.DateSave(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            try {
                DummyContent.saveDate(save, this.getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (wifiConnected || mobileConnected) {
            new XmlLoad(this.getApplicationContext()).execute(URL);

        } else {
            wakeLock.release();
        }


        return START_STICKY;
    }

    class ActiveBind extends Binder {
        public UpdateService getUpdateService() {
            return UpdateService.this;
        }
    }

    interface Update {
        // --Commented out by Inspection (6/15/13 9:03 PM):public void update();
    }

    void Notificate(int no, int pos) {
        long[] pattern = {200, 200, 500, 300, 1000};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.designcluster)
                        .setContentTitle("New Announcements")
                        .setContentText("Click to view").setNumber(no)
                        .setAutoCancel(true)
                        .setVibrate(pattern);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, PageListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("update", "no");
        if (pos != -1)
            bundle.putInt("showfield", pos);
        resultIntent.putExtras(bundle);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent1 = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.

        mBuilder.setContentIntent(intent1);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(5, mBuilder.build());
        try {
            wakeLock.release();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}



