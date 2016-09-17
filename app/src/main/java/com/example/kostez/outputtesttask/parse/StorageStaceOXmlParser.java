package com.example.kostez.outputtesttask.parse;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostez on 14.09.2016.
 */
public class StorageStaceOXmlParser {

    private List<Quote> quotes = new ArrayList();

    private static final String ns = null;

    public void parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readFeed(parser);
        } finally {
            in.close();
        }
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        int totalPages;

        String name = parser.getName();

        System.out.println("--- ");

        parser.nextTag();
        name = parser.getName();
        String text = readString(parser);

        parser.nextTag();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            name = parser.getName();

            if (name.equals("quote")) {
                quotes.add(readQuote(parser));
            } else {
                skip(parser);
            }
        }

        System.out.println(text);
    }

    private Quote readQuote(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "quote");
        int id = 0;
        String date = null;
        String text = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                id = readId(parser);
            } else if (name.equals("date")) {
                date = readData(parser);
            } else if (name.equals("text")) {
                text = readText(parser);
            } else {
                skip(parser);
            }
        }
        return new Quote(id, date, text);
    }

    private int readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "id");
        int id = Integer.parseInt(readString(parser));
        parser.require(XmlPullParser.END_TAG, ns, "id");
        return id;
    }

    private String readData(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "date");
        String date = readString(parser);
        parser.require(XmlPullParser.END_TAG, ns, "date");
        return date;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "text");
        int token = parser.nextToken();
        String cdata = parser.getText();
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "text");
        return cdata;
    }

    private String readString(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
}
