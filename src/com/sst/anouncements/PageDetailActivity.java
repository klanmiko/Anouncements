package com.sst.anouncements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class PageDetailActivity extends FragmentActivity {
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private pageradapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pager);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        /*mPager = (ViewPager) findViewById(R.id.pagerf);
        mPagerAdapter = new pageradapter(getSupportFragmentManager(),this,getIntent().getIntExtra(PageDetailFragment.pos,0));
        mPager.setAdapter(mPagerAdapter);*/

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(PageDetailFragment.ARG_ITEM_ID, getIntent()
                    .getStringExtra(PageDetailFragment.ARG_ITEM_ID));
            arguments.putString(PageDetailFragment.link, getIntent()
                    .getStringExtra(PageDetailFragment.link));
            PageDetailFragment fragment = new PageDetailFragment(this);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.page_detail_container, fragment).commit();
        }
    }
    /*@Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this,
                    new Intent(this, PageListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
