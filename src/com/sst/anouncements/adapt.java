package com.sst.anouncements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sst.anouncements.dummy.DummyContent;
import com.sst.anouncements.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

public class adapt extends ArrayAdapter<DummyContent.DummyItem> {
    Context context;
    int layoutResourceId;
    List<DummyItem> objects = new ArrayList<DummyItem>();

    public adapt(Context context, int resource, List<DummyItem> iTEMS) {
        super(context, resource, DummyContent.ITEM);
        this.layoutResourceId = resource;
        this.context = context;
        this.objects = DummyContent.ITEM;
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
            textHold hold = new textHold();
            hold.desc = (TextView) v.findViewById(R.id.Desc);
            hold.name = (TextView) v.findViewById(R.id.Post);
            if (hold.name != null) {
                hold.name.setText(i.content);
            }
            if (hold.desc != null) {
                if (i.description.length() > 50)
                    hold.desc.setText(i.description.substring(0, 50) + "...");
                else
                    hold.desc.setText(i.description);
            }
            assert hold.desc != null;
            hold.desc.setVisibility(View.GONE);
        }
        return v;
    }

    public int getCount() {
        return DummyContent.ITEM.size();
    }

    static class textHold {
        TextView desc;
        TextView name;
    }

}
