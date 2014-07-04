package com.sst.anouncements.dummy;

import android.content.Context;

import com.sst.anouncements.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DummyContent {
    private static String[] categories;
    private static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    //public static List<DummyItem> ITEM = new ArrayList<DummyItem>();
    private static List<Integer> POINTER = new ArrayList<Integer>();
    private static String cat = "All";
    private static ArrayList<Notify> adapters = new ArrayList<Notify>();

    public static boolean contains(DummyItem item) {
        if (ITEMS.contains(item))
            return true;
        else
            return false;
    }

    public static void addItem(DummyItem item) {

        ITEMS.add(0, item);
        //TODO allow adding arrays
        if (cat.equalsIgnoreCase("All")) {
            //ITEM.add(item);
            POINTER.add(ITEMS.indexOf(item));

        } else if (cat.equalsIgnoreCase("General")) {
            if (!item.content.toLowerCase(Locale.getDefault()).contains(
                    "Info Hub".toLowerCase(Locale.getDefault())))

            {
                //ITEM.add(item);
                POINTER.add(ITEMS.indexOf(item));

            }
        } else {
            if (item.content.toLowerCase(Locale.getDefault()).contains(
                    cat.toLowerCase(Locale.getDefault()))) {
                //ITEM.add(item);
                POINTER.add(ITEMS.indexOf(item));

            }
        }

        for (Notify notify : adapters) {
            notify.notifyupdate();
        }
    }

    public static void setContent(String cata) {
        //List<DummyItem> me = new ArrayList<DummyItem>();
        POINTER = new ArrayList<Integer>();
        for (DummyItem item : ITEMS) {
            if (cata.equalsIgnoreCase("All")) {
                //me.add(item);
                POINTER.add(ITEMS.indexOf(item));
            } else if (cata.equalsIgnoreCase("General")) {
                for (String catb : categories) {
                    if (!item.content.toLowerCase(Locale.getDefault()).contains(
                            catb.toLowerCase(Locale.getDefault())))
                        continue;

                    POINTER.add(ITEMS.indexOf(item));
                }
            } else {
                if (item.content.toLowerCase(Locale.getDefault()).contains(
                        cata.toLowerCase(Locale.getDefault()))) {
                    //me.add(item);
                    POINTER.add(ITEMS.indexOf(item));
                }
            }

        }
        cat = cata;
        //ITEM = me;
        // me = null;
        for (Notify notify : adapters) {
            notify.notifyupdate();
        }

    }

    public static void dump(Context activity) throws IOException {
        File out;
        out = new File(activity.getCacheDir(), "pages1.6");
        FileOutputStream f;
        f = new FileOutputStream(out);
        ObjectOutputStream o;
        o = new ObjectOutputStream(f);
        o.writeObject(ITEMS);
        o.close();
    }

    public static int findByAuthor(String content) {
        if (content == null)
            return -1;
        for (DummyItem itema : ITEMS) {
            if (itema.content.equalsIgnoreCase(content)) {
                return ITEMS.indexOf(itema);
            }
        }
        return -1;
    }

    public static void load(Context context) throws IOException, ClassNotFoundException {

        try {
            File out = new File(context.getCacheDir(), "pages1.6");
            FileInputStream f = new FileInputStream(out);
            ObjectInputStream o = new ObjectInputStream(f);
            ITEMS = (List<DummyItem>) o.readObject();
            o.close();
        } catch (FileNotFoundException e) {
            invalidate(context);
        }
        categories = context.getResources().getStringArray(R.array.action_list);
        DummyContent.setContent("All");
    }

    /*public static List<DummyItem> getContent() {
        return ITEM;
    }*/

    public static void addadapter(Notify notify) {
        adapters.add(notify);
    }

    public static String getActiveAuthor(int pos) {
        return ITEMS.get(POINTER.get(pos)).author;
    }

    public static String getActiveTitle(int pos) {
        return ITEMS.get(POINTER.get(pos)).content;
    }

    public static String getActiveLink(int pos) {
        return ITEMS.get(POINTER.get(pos)).link;
    }

    public static String getActiveDescription(int pos) {
        return ITEMS.get(POINTER.get(pos)).description;
    }

    public static void setReadActive(boolean truth, int pos) {
        ITEMS.get(POINTER.get(pos)).read = truth;
        for (Notify notify : adapters) {
            notify.notifyupdate();
        }
    }

    public static String getActiveId(int pos) {
        return ITEMS.get(pos).id;
    }

    public static int getActiveSize() {
        return POINTER.size();
    }

    public static DummyItem getActiveItem(int pos) {
        return ITEMS.get(POINTER.get(pos));
    }

    public static void invalidate(Context context) {
        try {
            File dir = new File(context.getCacheDir(), ".");
            for (File file : dir.listFiles()) {
                if (file.getName().startsWith("pages")) {
                    file.delete();
                }
            }
        } catch (Exception ef) {
            ef.printStackTrace();
        }
        ITEMS.clear();
        POINTER.clear();
        for (Notify notify : adapters) {
            notify.notifyupdate();
        }
    }

    public static DateSave loadDate(Context context) throws IOException, ClassNotFoundException {
        DateSave returns = null;
        try {
            File out = new File(context.getCacheDir(), "date");
            FileInputStream f = new FileInputStream(out);
            ObjectInputStream o = new ObjectInputStream(f);
            returns = (DateSave) o.readObject();
            o.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return returns;
    }

    public static void saveDate(DateSave save, Context context) throws IOException {
        File out;
        out = new File(context.getCacheDir(), "date");
        FileOutputStream f;
        f = new FileOutputStream(out);
        ObjectOutputStream o;
        o = new ObjectOutputStream(f);
        o.writeObject(save);
        o.close();
    }

    public static interface Notify {
        public void notifyupdate();
    }

    static {
    }

    public static class DateSave implements Serializable {
        public final int day, month, year;

        public DateSave(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

    }

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

    //TODO CHANGE TO A SQL BASED DATA TABLE SOURCE SO THAT WE CAN USE SERVER SQL
    //uhh probably would want to store URL, TITLE AND AUTHOR IN SQL SERVER. GRAB DATA TO SQL SERVER FROM BLOG OR FORM. CLIENT USES URL TO FETCH DESCRIPTION + LOCAL STORAGE.
    //TODO OPTION FOR ONLINE ONLY, SETTINGS.XML
}
