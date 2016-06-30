package com.sst.anouncements;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sst.anouncements.dummy.DummyContent;

import java.util.ArrayList;

public class PageListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    //empty listener to catch events when unbound
    private static final Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id, int position, View view) {
        }

        @Override
        public void fragmentlist(PageListFragment frag) {

        }
        @Override
        public void refresh()
        {

        }

    };
    public String cat = "All";
    SwipeRefreshLayout layout;
    private final BroadcastReceiver updatereceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("update").equals("post")) {
                layout.setRefreshing(false);
                setCategory(cat);
            } else if (intent.getStringExtra("update").equals("pre")) {
                layout.setRefreshing(true);
            }
        }
    };
    // --Commented out by Inspection (6/15/13 9:03 PM):public DataSetObserver observe;
    ArrayList<Callbacks> callbacks = new ArrayList<Callbacks>();
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private adapt adapter = null;
    public PageListFragment() {
    }

    @Override
    public void onRefresh() {
        mCallbacks.refresh();
    }

    public void registerlistener(Callbacks callback) {
        callbacks.add(callback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCategory("All");
        adapter = new adapt(getActivity(),
                com.sst.anouncements.R.layout.item_row, this);
        setListAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout = (SwipeRefreshLayout) view.findViewById(R.id.container);
        layout.setOnRefreshListener(this);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
        mCallbacks.fragmentlist(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);
        for (Callbacks callback : callbacks) {
            callback.onItemSelected(DummyContent.getActiveId(position), position, view);
        }
        mCallbacks.onItemSelected(DummyContent.getActiveId(position),
                position, view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public void setCategory(String cat) {
        this.cat = cat;
        DummyContent.setContent(cat);
    }

    public void showLoad() {
        layout.setRefreshing(true);
    }

    public void hideLoad() {
        layout.setRefreshing(false);
    }

    public void notifyupdate() {
        adapter.notifyupdate();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager brm = LocalBroadcastManager.getInstance(this.getActivity());
        brm.registerReceiver(updatereceiver, new IntentFilter("com.sst.announcements.UPDATE"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager brm = LocalBroadcastManager.getInstance(this.getActivity());
        brm.unregisterReceiver(updatereceiver);
    }

    public interface Callbacks {
        void onItemSelected(String id, int position, View view);
        void fragmentlist(PageListFragment frag);
        void refresh();
    }

}
