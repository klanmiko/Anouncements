package com.sst.anouncements;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class PageDetailActivity extends FragmentActivity {
    private Activity parent;
    private PageDetailFragment fragment;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_page_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        parent = this.getParent();
        if (parent instanceof PageListActivity) {
        }
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(PageDetailFragment.pos, this.getIntent().getIntExtra(PageDetailFragment.pos, 0));
            fragment = new PageDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.page_detail_container, fragment).commit();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
