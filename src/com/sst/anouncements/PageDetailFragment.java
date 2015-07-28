package com.sst.anouncements;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sst.anouncements.dummy.DummyContent;

public class PageDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String link = "link";
    public static final String pos = "position";
    private static String URL;
    private boolean wifiConnected = false;

    public PageDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        wifiConnected = Network.getNetwork();
        if (getArguments().containsKey(link)) {
            URL = getArguments().getString(link);
        } else {
            URL = DummyContent.getActiveLink(getArguments().getInt(pos));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_detail,
                container, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        boolean aBoolean = sharedPref.getBoolean("pref_offlinemode", true);
        int position = getArguments().getInt(pos);
        TextView title = (TextView) rootView.findViewById(R.id.titleview);
        TextView author = (TextView) rootView.findViewById(R.id.authorname);
        TextView content = (TextView) rootView.findViewById(R.id.content);
        WebView view = (WebView) rootView.findViewById(R.id.webView1);
        title.setText(DummyContent.getActiveTitle(position));
        author.setText(DummyContent.getActiveAuthor(position));
        content.setText(DummyContent.getActiveDescription(position));
        if (wifiConnected && !aBoolean) {
            if (URL != null) {
                assert rootView != null;
                view.setWebViewClient(new WebViewClient());
                WebSettings webSettings = view.getSettings();
                webSettings.setJavaScriptEnabled(true);
                view.loadUrl(URL);
            }
        } else {
            view.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            author.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch:
                View rootView = getView();
                TextView title = (TextView) rootView.findViewById(R.id.titleview);
                TextView author = (TextView) rootView.findViewById(R.id.authorname);
                TextView content = (TextView) rootView.findViewById(R.id.content);
                WebView web = (WebView) rootView.findViewById(R.id.webView1);
                if (web.getVisibility() == View.GONE) {
                    web.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                    author.setVisibility(View.GONE);
                    content.setVisibility(View.GONE);
                    web.setWebViewClient(new WebViewClient());
                    WebSettings webSettings = web.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    web.loadUrl(URL);
                } else if (web.getVisibility() == View.VISIBLE) {
                    web.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    author.setVisibility(View.VISIBLE);
                    content.setVisibility(View.VISIBLE);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
