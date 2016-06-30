package com.sst.anouncements;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sst.anouncements.dummy.DummyContent;
import com.sst.anouncements.dummy.DummyContent.DummyItem;

public class adapt extends ArrayAdapter<DummyContent.DummyItem> implements DummyContent.Notify {
    private final Context context;
    private final int layoutResourceId;
    private PageListFragment fragment;

    public adapt(Context context, int resource, PageListFragment fragment) {
        super(context, resource);
        this.layoutResourceId = resource;
        this.context = context;
        this.fragment = fragment;
        DummyContent.addadapter(this);
        // TODO Auto-generated constructor stub
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_row, null);
        }
        DummyItem i = DummyContent.getActiveItem(position);

        if (i != null) {
            final textHold hold = new textHold();
            assert v != null;
            hold.desc = (TextView) v.findViewById(R.id.Desc);
            hold.name = (TextView) v.findViewById(R.id.Post);
            hold.author = (TextView) v.findViewById(R.id.author);
            hold.positiona = position;
            if (hold.name != null) {
                hold.name.setText(i.content);
                if (!i.read) {
                    hold.name.setTypeface(null, Typeface.BOLD);
                    v.setBackgroundColor(Color.WHITE);
                } else if (i.read) {
                    hold.name.setTypeface(null, Typeface.NORMAL);
                    v.setBackgroundColor(Color.TRANSPARENT);
                }
            }
            if (hold.desc != null) {
                hold.desc.setText(i.description);

                hold.desc.setVisibility(View.GONE);
            }
            if (hold.author != null) {
                hold.author.setText(i.author);
            }
            assert hold.desc != null;
        }
        return v;
    }

    public int getCount() {
        return DummyContent.getActiveSize();
    }

    @Override
    public void notifyupdate() {
        this.notifyDataSetChanged();
    }

    public class textHold {
        TextView desc;
        TextView name;
        TextView author;
        int positiona;
        PageListFragment.Callbacks callback = new PageListFragment.Callbacks() {
            @Override
            public void onItemSelected(String id, int position, View view) {
                if (positiona == position) {
                    name.setTypeface(null, Typeface.NORMAL);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    DummyContent.setReadActive(true, position);
                }
            }
            @Override
            public void fragmentlist(PageListFragment frag) {
                // TODO Auto-generated method stub

            }
            @Override
            public void refresh()
            {

            }
        };
        public textHold() {
            fragment.registerlistener(callback);
        }
    }

}
