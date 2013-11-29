package com.sst.anouncements;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.sst.anouncements.dummy.DummyContent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by eternitysst on 6/2/13.
 */
class XmlLoad extends AsyncTask<String, Void, List<Announcement>> {
    private final Context activity;
    // --Commented out by Inspection (6/15/13 9:03 PM):boolean cached = false;
    // --Commented out by Inspection (6/15/13 9:03 PM):int param;

    public XmlLoad(Context context) {
        this.activity = context;

    }

    @Override
    protected void onPreExecute() {


    }

    @Override
    protected List<Announcement> doInBackground(String... params) {
        // TODO Auto-generated method stub
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        String syncConnPref = sharedPref.getString("pref_key_refresh", "0");
        Integer no = new Integer(syncConnPref);
        if (no != 0) {
            try {
                return loadXml(params[0]);
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
            return null;
        }
        return null;
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

    @Override
    protected void onPostExecute(List<Announcement> result) {
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
            DummyContent.dump(activity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent2 = new Intent("com.sst.announcements.UPDATE");
        intent2.putExtra("update", "post");
        activity.sendBroadcast(intent2);
        Intent intent = new Intent("com.sst.announcements.UPDATED");
        intent.putExtra("new", counter.toString());
        if (last != null) {
            intent.putExtra("showfield", last.title);
        }
        activity.sendBroadcast(intent);
    }
}
