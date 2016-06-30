package com.sst.anouncements;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.sst.anouncements.dummy.DummyContent;

import java.io.File;
import java.io.IOException;

public class PageListActivity extends FragmentActivity implements
        PageListFragment.Callbacks{
    private boolean mTwoPane;
    private boolean wifiConnected = false;
    private boolean mobileConnected = false;
    private boolean first = true;
    private boolean load = true;
    private PageListFragment fragment;
    private int notifpos = -1;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */

    void refreshPage() {
        if (wifiConnected || mobileConnected) {
            Intent updateIntent = new Intent(this,UpdateIntentService.class);
            this.startService(updateIntent);
        } else {
            Toast.makeText(this, "Need Internet", Toast.LENGTH_SHORT).show();
        }
    }
    void loadPage() {
        fragment.showLoad();
        try{
            DummyContent.load(this.getApplicationContext());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            refreshPage();
            DummyContent.setContent("All");
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
        first = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        this.setContentView(R.layout.activity_page_list);
        Intent tent = new Intent("com.sst.anouncements.STARTUPDATE");
        this.sendBroadcast(tent);
        Network.init(this);
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
        loadPage();
        try {
            File httpCacheDir = new File(this.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            e.printStackTrace();

        }
        if (findViewById(R.id.page_detail_container) != null) {
            mTwoPane = true;


            ((PageListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.page_list))
                    .setActivateOnItemClick(true);
            Bundle arguments = new Bundle();
            arguments.putString(PageDetailFragment.link, DummyContent.getActiveLink(0));
            arguments.putInt(PageDetailFragment.pos, 0);
            PageDetailFragment fragger = new PageDetailFragment();
            fragger.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.page_detail_container, fragger).commit();


        }


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
        Network.init(this);


    }
    @Override
    public void onItemSelected(String id, int position, View view) {
        View title = view.findViewById(R.id.Post);
        View author = view.findViewById(R.id.author);
        DummyContent.setReadActive(true, position);
        fragment.notifyupdate();
        if (mTwoPane) {
            Bundle arguments = new Bundle();

            arguments.putString(PageDetailFragment.ARG_ITEM_ID, id);
            arguments.putString(PageDetailFragment.link, DummyContent.getActiveLink(position));
            arguments.putInt(PageDetailFragment.pos, position);
            PageDetailFragment fragger = new PageDetailFragment();
            fragger.setArguments(arguments);
            FragmentTransaction a = getSupportFragmentManager().beginTransaction();
            a.addSharedElement(title, "titletrans");
            a.addSharedElement(author, "authortrans");
            a.replace(R.id.page_detail_container, fragger);
            a.commit();
        } else {
            Intent detailIntent = new Intent(this, PageDetailActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    Pair.create(title, "titletrans"),
                    Pair.create(author, "authortrans"));
            detailIntent.putExtra(PageDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(PageDetailFragment.link,
                    DummyContent.getActiveLink(position));
            detailIntent.putExtra(PageDetailFragment.pos, position);
            startActivity(detailIntent, options.toBundle());
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
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsFragment.class);
                this.startActivity(intent);
                return true;
            case R.id.action_about:
                Intent intent1 = new Intent(this, AboutActivity.class);
                this.startActivity(intent1);
                return true;
            case R.id.menu_refresh:
                refresh();

        }
        return false;

    }
    @Override
    public void fragmentlist(PageListFragment frag) {
        this.fragment = frag;
    }

    @Override
    public void refresh() {
        refreshPage();
    }
}
