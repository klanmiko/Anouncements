package com.sst.anouncements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sst.anouncements.dummy.DummyContent;
import com.sst.anouncements.dummy.DummyContent.DummyItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GetXml extends AsyncTask<String, Void, List<Announcement>> {
    Context activity;
    boolean cached = false;
    int param;

    public GetXml(Context context) {
        this.activity = context;
        this.param = param;
    }

    @Override
    protected void onPreExecute() {


        Intent intent = new Intent("com.sst.announcements.UPDATE");
        intent.putExtra("update", "pre");

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);


    }

    @Override
    protected List<Announcement> doInBackground(String... params) {
        // TODO Auto-generated method stub
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

    public static InputStream downloadUrl(String url) throws IOException {
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
        for (Announcement announ : result) {
            DummyContent.addItem(new DummyItem(counter.toString(),
                    announ.title, announ.desc, announ.link, announ.author));
            counter++;
        }

        Intent intent = new Intent("com.sst.announcements.UPDATE");
        intent.putExtra("update", "post");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);


    }

}
