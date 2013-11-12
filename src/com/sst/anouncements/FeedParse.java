package com.sst.anouncements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.util.Xml;

import com.sst.anouncements.dummy.DummyContent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class FeedParse {
    public List<Announcement> parse(InputStream in)
            throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Announcement> readFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub

        List<Announcement> entries = new ArrayList<Announcement>();

        parser.require(XmlPullParser.START_TAG, null, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                Announcement entry = readEntry(parser);
                if (DummyContent.ITEMS.contains(new DummyContent.DummyItem("2", entry.title, entry.desc, entry.link, entry.author))) {
                    return entries;
                } else {
                    entries.add(0, entry);
                }

            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private final boolean stop = false;

    private Announcement readEntry(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        parser.require(XmlPullParser.START_TAG, null, "entry");
        String title = "";
        String summary = "";
        String link = "";
        String author = "";
        while (parser.next() != XmlPullParser.END_TAG && (!stop)) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("content")) {
                summary = readSummary(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("author")) {
                author = readAuthor(parser);
            } else {
                skip(parser);
            }

        }
        return new Announcement(link, title, summary, author);
    }

    private String readLink(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        String link = "";
        parser.require(XmlPullParser.START_TAG, null, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");


            }
        }
        parser.nextTag();
        try {
            parser.require(XmlPullParser.END_TAG, null, "link");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return link;
    }

    private String readSummary(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        parser.require(XmlPullParser.START_TAG, null, "content");
        String content = Html.fromHtml(readText(parser)).toString();
        parser.require(XmlPullParser.END_TAG, null, "content");
        return content;
    }

    private String readTitle(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        // TODO Auto-generated method stub
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // TODO Auto-generated method stub
    private void skip(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readAuthor(XmlPullParser parser) throws XmlPullParserException, IOException {
        String author = "";
        parser.require(XmlPullParser.START_TAG, null, "author");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("name")) {
                parser.require(XmlPullParser.START_TAG, null, "name");
                author = Html.fromHtml(readText(parser)).toString();
                parser.require(XmlPullParser.END_TAG, null, "name");
            } else {
                skip(parser);
            }


        }

        return author;

    }
}
