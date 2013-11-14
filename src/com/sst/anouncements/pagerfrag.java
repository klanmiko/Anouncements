package com.sst.anouncements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eternitysst on 11/11/13.
 */
public class pagerfrag extends Fragment {
    private ViewPager mPager;
    private PageListActivity parent;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private pageradapter mPagerAdapter;

    public pagerfrag() {

    }

    public void setParent(PageListActivity parent1) {
        this.parent = parent1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager,
                container, false);
        mPager = (ViewPager) container.findViewById(R.id.pagerf);
        mPagerAdapter = new pageradapter(parent.getSupportFragmentManager(), getArguments().getInt(PageDetailFragment.pos, 0));
        mPager.setAdapter(mPagerAdapter);
        return rootView;
    }
}
