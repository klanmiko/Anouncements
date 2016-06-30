package com.sst.anouncements;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;

import com.sst.anouncements.dummy.DummyContent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by eternitysst on 6/30/16.
 */
public class UpdateIntentService extends IntentService {
    private PowerManager.WakeLock wakeLock;

    public UpdateIntentService() {
        super("hi");

    }


    @Override
    protected void onHandleIntent(Intent intent) {
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
            cal.add(GregorianCalendar.MONTH, 12);
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
                save = new DummyContent.DateSave(calnow.get(GregorianCalendar.DAY_OF_MONTH), calnow.get(GregorianCalendar.MONTH), calnow.get(GregorianCalendar.YEAR));
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
            save = new DummyContent.DateSave(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            try {
                DummyContent.saveDate(save, this.getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (wifiConnected || mobileConnected) {
            try {
                preProcess();
                processResults(loadXml(URL));
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }

        } else {
            wakeLock.release();
        }

    }
    private static InputStream downloadUrl(String url) throws IOException {
        // TODO Auto-generated method stub
        URL uRL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uRL.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream is = conn.getInputStream();
        return is;
    }


    protected void preProcess() {
        Intent intent2 = new Intent("com.sst.announcements.UPDATE");
        intent2.putExtra("update", "pre");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
    }
    private List<Announcement> loadXml(String url)
            throws XmlPullParserException, IOException {
        InputStream stream = null;
        FeedParse parse = new FeedParse();
        try {
            stream = downloadUrl(url);
            return parse.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

    }
    protected void processResults(List<Announcement> result) {
        try {
            Integer counter = 0;
            assert result != null;
            Announcement last = null;
            for (Announcement announ : result) {
                DummyContent.addItem(new DummyContent.DummyItem(counter.toString(),
                        announ.title, announ.desc, announ.link, announ.author));
                counter++;
                last = announ;
            }
            try {
                DummyContent.dump(this.getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent2 = new Intent("com.sst.announcements.UPDATE");
            intent2.putExtra("update", "post");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
            Intent intent = new Intent("com.sst.announcements.UPDATED");
            intent.putExtra("new", counter.toString());
            if (last != null) {
                intent.putExtra("showfield", last.title);
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (NullPointerException w) {
            w.printStackTrace();
        }
    }
}
