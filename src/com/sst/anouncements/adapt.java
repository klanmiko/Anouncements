package com.sst.anouncements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.graphics.Typeface;

import com.sst.anouncements.dummy.DummyContent;
import com.sst.anouncements.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

public class adapt extends ArrayAdapter<DummyContent.DummyItem> {
    private final Context context;
    private final int layoutResourceId;
    private PageListFragment fragment;
    private List<DummyItem> objects = new ArrayList<DummyItem>();

    public adapt(Context context, int resource, PageListFragment fragment) {
        super(context, resource, DummyContent.ITEM);
        this.layoutResourceId = resource;
        this.context = context;
        this.objects = DummyContent.ITEM;
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
        DummyItem i = DummyContent.ITEM.get(position);

        if (i != null) {
            final textHold hold = new textHold();
            assert v != null;
            hold.desc = (TextView) v.findViewById(R.id.Desc);
            hold.name = (TextView) v.findViewById(R.id.Post);
            hold.author = (TextView) v.findViewById(R.id.author);
            hold.button = (ToggleButton) v.findViewById(R.id.toggleButton);
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
            if (hold.button != null) {
                hold.button.setChecked(false);
                hold.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        hold.name.setTypeface(null, Typeface.NORMAL);
                        DummyContent.ITEM.get(hold.position).read = true;
                        ToggleButton mbutton = (ToggleButton) compoundButton;
                        boolean on = mbutton.isChecked();
                        if (on) {
                            hold.desc.setVisibility(View.VISIBLE);

                        } else {
                            hold.desc.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
        return v;
    }

    public int getCount() {
        return DummyContent.ITEM.size();
    }

    public class textHold {
        TextView desc;
        TextView name;
        TextView author;
        ToggleButton button;
        int position;
        PageListFragment.Callbacks callback = new PageListFragment.Callbacks() {
            @Override
            public void onItemSelected(String id, int position) {
                name.setTypeface(null, Typeface.NORMAL);
                DummyContent.ITEM.get(position).read = true;
            }

            @Override
            public void fragment(PageListFragment frag) {
                // TODO Auto-generated method stub

            }
        };

        public textHold() {
            fragment.registerlistener(callback);
        }
    }

}
