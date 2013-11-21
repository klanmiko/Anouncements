package com.sst.anouncements.dummy;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DummyContent {

    public static class DummyItem implements Serializable {

        public final String id;
        public final String content;
        public final String description;
        public final String link;
        public final String author;
        public boolean read = false;


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

    public static interface Notify {
        public void notifyupdate();
    }

    static {
    }

    private static ArrayList<Notify> adapters = new ArrayList<Notify>();

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
        for (Notify notify : adapters) {
            notify.notifyupdate();
        }

    }

    public static void dump(Context activity) throws IOException {
        File out;
        out = new File(activity.getCacheDir(), "pages");
        FileOutputStream f;
        f = new FileOutputStream(out);
        ObjectOutputStream o;
        o = new ObjectOutputStream(f);
        o.writeObject(ITEM_MAP);
        o.writeObject(ITEMS);
        o.close();
    }

    public static int findByAuthor(String author) {
        if (author == null)
            return -1;
        for (DummyItem itema : ITEMS) {
            if (itema.author == author) {
                return ITEMS.indexOf(itema);
            }
        }
        return -1;
    }

    public static void load(Context context) throws IOException, ClassNotFoundException {

        File out = new File(context.getCacheDir(), "pages");
        FileInputStream f = new FileInputStream(out);
        ObjectInputStream o = new ObjectInputStream(f);
        ITEM_MAP = (Map<String, DummyItem>) o.readObject();
        ITEMS = (List<DummyItem>) o.readObject();
        o.close();
        DummyContent.setContent("All");
    }

    public static void addadapter(Notify notify) {
        adapters.add(notify);
    }
}
