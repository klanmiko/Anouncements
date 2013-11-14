package com.sst.anouncements;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sst.anouncements.dummy.DummyContent;

class PageDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private static String URL;
    public static final String link = "link";
    public static final String pos = "position";
    private boolean wifiConnected = false;

    // --Commented out by Inspection (6/15/13 9:03 PM):private DummyContent.DummyItem mItem;

    public PageDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        wifiConnected = Network.getNetwork();
        if (getArguments().containsKey(link)) {
            URL = getArguments().getString(link);
        } else {
            URL = DummyContent.ITEM.get((getArguments().getInt(pos))).link;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_detail,
                container, false);
        if (wifiConnected) {
            if (URL != null) {
            assert rootView != null;
            WebView view = (WebView) rootView.findViewById(R.id.webView1);
            view.setWebViewClient(new WebViewClient());
            WebSettings webSettings = view.getSettings();
            webSettings.setJavaScriptEnabled(true);
            view.loadUrl(URL);

            }
        } else {
            int position = getArguments().getInt(pos);
            WebView view = (WebView) rootView.findViewById(R.id.webView1);
            view.setVisibility(View.GONE);
            TextView title = (TextView) rootView.findViewById(R.id.titleview);
            TextView author = (TextView) rootView.findViewById(R.id.authorname);
            TextView content = (TextView) rootView.findViewById(R.id.content);
            title.setText(DummyContent.ITEM.get(position).content);
            author.setText(DummyContent.ITEM.get(position).author);
            content.setText(DummyContent.ITEM.get(position).description);
            title.setVisibility(View.VISIBLE);
            author.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);


        }
        return rootView;
    }
}
