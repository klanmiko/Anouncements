package com.sst.anouncements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sst.anouncements.dummy.DummyContent;

import java.util.ArrayList;

public class PageListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    // --Commented out by Inspection (6/15/13 9:03 PM):public static final String category = "Category";
    public String cat = "all";
    private adapt adapter = null;
    // --Commented out by Inspection (6/15/13 9:03 PM):public DataSetObserver observe;
    ArrayList<Callbacks> callbacks = new ArrayList<Callbacks>();

    public interface Callbacks {

        public void onItemSelected(String id, int position);

        public void fragmentlist(PageListFragment frag);
    }

    public void registerlistener(Callbacks callback) {
        callbacks.add(callback);
    }

    private static final Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id, int position) {
        }

        @Override
        public void fragmentlist(PageListFragment frag) {
            // TODO Auto-generated method stub

        }
    };

    public PageListFragment() {
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
            callback.onItemSelected(DummyContent.getActiveId(position), position);
        }
        mCallbacks.onItemSelected(DummyContent.getActiveId(position),
                position);
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
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void notifyupdate() {
        //TODO change dummycontent so that ITEM reflects changes to ITEMS
        DummyContent.setContent(cat);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void showLoad() {
        ProgressBar bar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        TextView text = (TextView) getActivity().findViewById(R.id.progresstext);
        bar.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
    }

    public void hideLoad() {
        ProgressBar bar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        TextView text = (TextView) getActivity().findViewById(R.id.progresstext);
        bar.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
    }

}
