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
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private pageradapter mPagerAdapter;

    public pagerfrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager,
                container, false);
        mPager = (ViewPager) rootView.findViewById(R.id.pagerview);
        if (this.getArguments() == null || this.getArguments().containsKey(PageDetailFragment.pos) == false) {
            mPagerAdapter = new pageradapter(this.getActivity().getSupportFragmentManager(), 0);

        } else {
            mPagerAdapter = new pageradapter(this.getActivity().getSupportFragmentManager(), getArguments().getInt(PageDetailFragment.pos, 0));
        }
        if (mPagerAdapter == null) {
            throw new NullPointerException();
        } else if (mPager == null) {
            throw new NullPointerException();
        } else {
            mPager.setAdapter(mPagerAdapter);
        }
        return rootView;
    }
}
