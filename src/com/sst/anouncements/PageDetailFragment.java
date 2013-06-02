package com.sst.anouncements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sst.anouncements.dummy.DummyContent;

public class PageDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private static String URL;
    public static final String link = "link";

    private DummyContent.DummyItem mItem;

    public PageDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
                    ARG_ITEM_ID));
        }
        if (getArguments().containsKey(link)) {
            URL = getArguments().getString(link);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_detail,
                container, false);
        if (URL != null) {
            WebView view = (WebView) rootView.findViewById(R.id.webView1);
            view.setWebViewClient(new WebViewClient());
            WebSettings webSettings = view.getSettings();
            webSettings.setJavaScriptEnabled(true);
            view.loadUrl(URL);

        }
        return rootView;
    }
}
