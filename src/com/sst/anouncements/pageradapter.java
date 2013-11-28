package com.sst.anouncements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.sst.anouncements.dummy.DummyContent;

/**
 * Created by eternitysst on 11/11/13.
 */
public class pageradapter extends FragmentStatePagerAdapter implements DummyContent.Notify {
    private int start;
    private pagerfrag parent;

    public pageradapter(FragmentManager fm, int start, pagerfrag parent) {
        super(fm);
        this.start = start;
        this.parent = parent;
        DummyContent.addadapter(this);
    }

    @Override
    public Fragment getItem(int i) {
        Bundle arguments = new Bundle();
        arguments.putInt(PageDetailFragment.pos, start + i);
        PageDetailFragment fragment = new PageDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        parent.flip();

    }

    @Override
    public int getCount() {
        return DummyContent.getActiveSize();
    }

    public void setStart(int i) {
        start = i;
    }

    @Override
    public void notifyupdate() {
        this.notifyDataSetChanged();
    }
}
