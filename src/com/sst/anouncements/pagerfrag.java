package com.sst.anouncements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by eternitysst on 11/11/13.
 */
public class pagerfrag extends Fragment {
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private pageradapter mPagerAdapter;
    ArrayList<Callbacks> callbacks = new ArrayList<Callbacks>();
    ViewGroup container;

    public pagerfrag() {

    }

    public interface Callbacks {

        public void fragment(pagerfrag frag);

        public void onItemSelected(int i);
    }

    public void registerlistener(Callbacks callback) {
        callbacks.add(callback);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        } else {
            ((Callbacks) activity).fragment(this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager,
                container, false);
        this.container = container;
        mPager = (ViewPager) rootView.findViewById(R.id.pagerview);
        if (this.getArguments() == null || this.getArguments().containsKey(PageDetailFragment.pos) == false) {
            mPagerAdapter = new pageradapter(this.getActivity().getSupportFragmentManager(), 0, this);

        } else {
            mPagerAdapter = new pageradapter(this.getActivity().getSupportFragmentManager(), getArguments().getInt(PageDetailFragment.pos, 0), this);
        }
        if (mPagerAdapter == null) {
            throw new NullPointerException();
        } else if (mPager == null) {
            throw new NullPointerException();
        } else {
            mPager.setAdapter(mPagerAdapter);
        }
        mPager.setFocusableInTouchMode(true);
        return rootView;
    }

    public void datasetchange() {
        if (mPagerAdapter != null) {
            mPagerAdapter.setStart(0);
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    public void flip() {
        for (Callbacks callback : callbacks) {
            callback.onItemSelected(mPager.getCurrentItem());
        }

    }

    public void setPage(int i) {
        mPagerAdapter.instantiateItem(container, i);
        mPager.setCurrentItem(i);
    }
}
