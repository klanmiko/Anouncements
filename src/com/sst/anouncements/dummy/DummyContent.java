package com.sst.anouncements.dummy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.sst.anouncements.PageListActivity;

import java.io.*;
import java.util.*;

public class DummyContent {

    public static class DummyItem implements Serializable {

        public String id;
        public String content;
        public String description;
        public String link;
        public String author;

        public DummyItem(String id, String content, String description,
                         String link, String author) {
            this.id = id;
            this.content = content;
            this.description = description;
            this.link = link;
            this.author = author;
        }

        @Override
        public String toString() {
            return content;
        }

        @Override
        public boolean equals(Object a) {
            if (a instanceof DummyItem) {
                DummyItem item = (DummyItem) a;
                if (this.content.equals(item.content) && this.description.equals(item.description) && this.link.equals(item.link) && this.author.equals(item.author)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }


        }
    }

    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static List<DummyItem> ITEM = new ArrayList<DummyItem>();
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
    }

    public static void addItem(DummyItem item) {

        ITEMS.add(0, item);
        ITEM_MAP.put(item.id, item);
    }

    public static List<DummyItem> getContent() {
        return ITEM;
    }

    public static void setContent(String cat) {
        List<DummyItem> me = new ArrayList<DummyItem>();
        for (DummyItem item : ITEMS) {

            if (cat.equalsIgnoreCase("All")) {
                me.add(item);

            } else if (cat.equalsIgnoreCase("General")) {
                if (!item.content.toLowerCase(Locale.getDefault()).contains(
                        "Info Hub".toLowerCase(Locale.getDefault())))

                {
                    me.add(item);

                }
            } else {
                if (item.content.toLowerCase(Locale.getDefault()).contains(
                        cat.toLowerCase(Locale.getDefault()))) {
                    me.add(item);

                }
            }

        }
        ITEM = me;

    }

    public static boolean dump(Context activity) throws IOException {
        File out;
        out = new File(activity.getCacheDir(), "pages");
        FileOutputStream f;
        f = new FileOutputStream(out);
        ObjectOutputStream o;
        o = new ObjectOutputStream(f);
        o.writeObject(ITEM_MAP);
        o.writeObject(ITEMS);
        o.close();
        return true;
    }

    public static boolean load(Context context) throws IOException, ClassNotFoundException {

        File out = new File(context.getCacheDir(), "pages");
        FileInputStream f = new FileInputStream(out);
        ObjectInputStream o = new ObjectInputStream(f);
        ITEM_MAP = (Map<String, DummyItem>) o.readObject();
        ITEMS = (List<DummyItem>) o.readObject();
        o.close();
        DummyContent.setContent("All");
        return true;
    }
}
