package com.sst.anouncements;

import android.app.ActionBar;
import android.content.*;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
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
        PageListFragment.Callbacks, UpdateService.update {

    private boolean mTwoPane;
    private boolean wifiConnected = false;
    private boolean mobileConnected = false;
    boolean first = true;
    boolean load = true;
    public PageListFragment fragment;
    public UpdateService service;
    ServiceConnection updateservice = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UpdateService.ActiveBind bind = (UpdateService.ActiveBind) iBinder;
            service = bind.getUpdateService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private BroadcastReceiver updatereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("update") == "pre")

            {
                preupdate();
            } else if (intent.getStringExtra("update") == "post") {

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


    public void refreshPage() {
        GetXml xml = new GetXml(this);

        if (wifiConnected || mobileConnected) {
            xml
                    .execute("http://sst-students2013.blogspot.com/feeds/posts/default");
        } else {
            Toast.makeText(this, "Need Internet", 5);

        }


    }

    public void loadPage() {

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
                if (bundle.getString("update") == "no") {
                    this.load = false;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        this.setContentView(R.layout.activity_page_list);
        Intent tent = new Intent("com.sst.anouncements.STARTUPDATE");
        this.sendBroadcast(tent);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfor = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiConnected = networkInfo.isConnected();
        mobileConnected = mobileInfor.isConnected();
        try {
            File httpCacheDir = new File(this.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {


        }

        loadPage();

        SpinnerAdapter sadapt = ArrayAdapter.createFromResource(this,
                R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);
        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(
                    R.array.action_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                // Create new fragment from our own Fragment class

                if (strings[position] != null && fragment != null) {
                    fragment.setCategory(strings[position]);

                } else {
                    if (fragment == null) {

                    }
                    if (strings[position] == null) {

                    }
                }
                return true;
            }
        };
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(sadapt, mOnNavigationListener);

        if (findViewById(R.id.page_detail_container) != null) {
            mTwoPane = true;
            ((PageListFragment) getSupportFragmentManager().findFragmentById(
                    R.id.page_list)).setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id, int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(PageDetailFragment.ARG_ITEM_ID, id);
            PageDetailFragment fragment = new PageDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.page_detail_container, fragment).commit();

        } else {
            Intent detailIntent = new Intent(this, PageDetailActivity.class);
            detailIntent.putExtra(PageDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(PageDetailFragment.link,
                    DummyContent.ITEM.get(position).link);
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

    public void preupdate() {
        fragment.showLoad();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            DummyContent.load(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.setContentView(R.layout.activity_page_list);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfor = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiConnected = networkInfo.isConnected();
        mobileConnected = mobileInfor.isConnected();
        SpinnerAdapter sadapt = ArrayAdapter.createFromResource(this,
                R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);
        ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(
                    R.array.action_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                // Create new fragment from our own Fragment class

                if (strings[position] != null && fragment != null) {
                    fragment.setCategory(strings[position]);

                } else {
                    if (fragment == null) {

                    }
                    if (strings[position] == null) {

                    }
                }
                return true;
            }
        };
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(sadapt, mOnNavigationListener);
        // Checks the orientation of the screen

    }
}
