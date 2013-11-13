package com.sst.anouncements;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.Service;
import android.content.*;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.sst.anouncements.dummy.DummyContent;

import java.io.File;
import java.io.IOException;

public class PageListActivity extends FragmentActivity implements
        PageListFragment.Callbacks, UpdateService.Update {

    private boolean mTwoPane;
    private boolean wifiConnected = false;
    private boolean mobileConnected = false;
    private boolean first = true;
    private boolean load = true;
    private PageListFragment fragment;
    private UpdateService service;
    private PageListActivity parent;
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private pageradapter mPagerAdapter;
    // --Commented out by Inspection (6/15/13 9:03 PM):private UpdateService service;
    private final ServiceConnection updateservice = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UpdateService.ActiveBind bind = (UpdateService.ActiveBind) iBinder;
            service = bind.getUpdateService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private final BroadcastReceiver updatereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("update").equals("pre"))

            {
                preupdate();
            } else if (intent.getStringExtra("update").equals("post")) {

                update();
            }


        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, UpdateService.class);

        bindService(intent, updateservice, Context.BIND_AUTO_CREATE);
    }


    void refreshPage() {
        GetXml xml = new GetXml(this);

        if (wifiConnected || mobileConnected) {
            xml
                    .execute("http://sst-students2013.blogspot.com/feeds/posts/default");
        } else {
            Toast.makeText(this, "Need Internet", Toast.LENGTH_SHORT).show();


        }


    }

    void loadPage() {

        try {
            fragment.showLoad();
            DummyContent.load(this);
            fragment.hideLoad();
            fragment.setCategory("All");
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        if (wifiConnected || mobileConnected) {
            new GetXml(this)
                    .execute("http://sst-students2013.blogspot.com/feeds/posts/default");
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager brm = LocalBroadcastManager.getInstance(this);
        brm.registerReceiver(updatereceiver, new IntentFilter("com.sst.announcements.UPDATE"));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager brm = LocalBroadcastManager.getInstance(this);
        brm.unregisterReceiver(updatereceiver);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        first = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.getString("update").equals("no")) {
                    this.load = false;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        PreferenceManager.setDefaultValues(this, R.layout.settings, false);

        this.setContentView(R.layout.activity_page_list);
        Intent tent = new Intent("com.sst.anouncements.STARTUPDATE");
        this.sendBroadcast(tent);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfor = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assert networkInfo != null;
        wifiConnected = networkInfo.isConnected();
        if (mobileInfor != null) {
            mobileConnected = mobileInfor.isConnected();
        }
        try {
            File httpCacheDir = new File(this.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            e.printStackTrace();

        }

        loadPage();

        SpinnerAdapter sadapt = ArrayAdapter.createFromResource(this,
                R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);
        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            final String[] strings = getResources().getStringArray(
                    R.array.action_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                // Create new fragment from our own Fragment class

                if (strings[position] != null && fragment != null) {
                    fragment.setCategory(strings[position]);

                }
                return true;
            }
        };
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(sadapt, mOnNavigationListener);

        if (findViewById(R.id.pagerf) != null) {
            mTwoPane = true;
            /*mPager = (ViewPager) findViewById(R.id.pagerf);
            mPagerAdapter = new pageradapter(getSupportFragmentManager(),this,0);
            mPager.setAdapter(mPagerAdapter);
            /*
            ((PageListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.page_list))
                    .setActivateOnItemClick(true);*/


        }
    }

    @Override
    public void onItemSelected(String id, int position) {
        if (mTwoPane) {
            DummyContent.ITEM.get(position).read = true;
            fragment.setCategory(fragment.cat);
           /* mPager = (ViewPager) findViewById(R.id.pagerf);
            mPagerAdapter = new pageradapter(parent.getSupportFragmentManager(),this,position);
            mPager.setAdapter(mPagerAdapter);
            /*
            Bundle arguments = new Bundle();
            arguments.putString(PageDetailFragment.ARG_ITEM_ID, id);
            arguments.putString(PageDetailFragment.link, DummyContent.ITEM.get(position).link);
            arguments.putInt(PageDetailFragment.pos, position);
            PageDetailFragment fragment = new PageDetailFragment(this);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.page_detail_container, fragment).commit();*/


        } else {
            Intent detailIntent = new Intent(this, PageDetailActivity.class);
            DummyContent.ITEM.get(position).read = true;
            fragment.setCategory(fragment.cat);
            detailIntent.putExtra(PageDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(PageDetailFragment.link,
                    DummyContent.ITEM.get(position).link);
            detailIntent.putExtra(PageDetailFragment.pos, position);
            startActivity(detailIntent);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            DummyContent.dump(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu item) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                this.refreshPage();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsFragment.class);
                this.startActivity(intent);
                return true;
            case R.id.action_about:
                Intent intent1 = new Intent(this, AboutActivity.class);
                this.startActivity(intent1);
                return true;

        }
        return false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(updateservice
        );
    }


    @Override
    public void fragment(PageListFragment frag) {
        // TODO Auto-generated method stub
        this.fragment = frag;
    }

    public void update() {
        fragment.hideLoad();
        fragment.setCategory(fragment.cat);
    }

    void preupdate() {
        fragment.showLoad();

    }


}
