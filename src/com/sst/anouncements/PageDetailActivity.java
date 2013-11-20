package com.sst.anouncements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class PageDetailActivity extends FragmentActivity implements pagerfrag.Callbacks {
    private Activity parent;
    private pagerfrag fragment;

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
            fragment.registerlistener((PageListActivity) parent);
        }
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(PageDetailFragment.pos, this.getIntent().getIntExtra(PageDetailFragment.pos, 0));
            fragment = new pagerfrag();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.page_detail_container, fragment).commit();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this,
                    new Intent(this, PageListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fragment(pagerfrag frag) {
    }

    @Override
    public void onItemSelected(int i) {

    }
}
