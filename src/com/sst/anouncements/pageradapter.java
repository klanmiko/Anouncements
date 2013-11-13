package com.sst.anouncements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sst.anouncements.dummy.DummyContent;

/**
 * Created by eternitysst on 11/11/13.
 */
public class pageradapter extends FragmentStatePagerAdapter {
    private Activity parent;
    private int start;

    public pageradapter(FragmentManager fm, Activity context, int start) {
        super(fm);
        this.parent = parent;
        this.start = start;

    }

    @Override
    public Fragment getItem(int i) {
        Bundle arguments = new Bundle();
        arguments.putInt(PageDetailFragment.pos, start + i);
        PageDetailFragment fragment = new PageDetailFragment(parent);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return DummyContent.ITEM.size();
    }
}
