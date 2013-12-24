package com.sst.anouncements;

import android.content.Context;
import android.graphics.Typeface;
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
        // TODO Auto-generated constructor stub
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_row, null);
        }

		/*
         * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
        DummyItem i = DummyContent.getActiveItem(position);

        if (i != null) {
            final textHold hold = new textHold();
            assert v != null;
            hold.desc = (TextView) v.findViewById(R.id.Desc);
            hold.name = (TextView) v.findViewById(R.id.Post);
            hold.author = (TextView) v.findViewById(R.id.author);
            hold.position = position;
            if (hold.name != null) {
                hold.name.setText(i.content);
                if (!i.read) {
                    hold.name.setTypeface(null, Typeface.BOLD);
                } else if (i.read) {
                    hold.name.setTypeface(null, Typeface.NORMAL);
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
        int position;
        PageListFragment.Callbacks callback = new PageListFragment.Callbacks() {
            @Override
            public void onItemSelected(String id, int position) {
                name.setTypeface(null, Typeface.NORMAL);
                DummyContent.setReadActive(true, position);
            }

            @Override
            public void fragmentlist(PageListFragment frag) {
                // TODO Auto-generated method stub

            }


        };

        public textHold() {
            fragment.registerlistener(callback);
        }
    }

}
