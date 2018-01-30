/*
 * Copyright (c) Aron Koszo 2018.
 *
 * This file is part of hassistant.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.reversedengineer.hassistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends AppCompatActivity {

    /** WebView hosting the main content. */
    private WebView mWebView;

    private static final String TAG = WebViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* set up the activity */
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_webview);

        /* try to restore the saved state */
        if (savedInstanceState != null) {
            ((WebView)findViewById(R.id.webview_main)).restoreState(savedInstanceState);
        }

        /* set up the toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Activity activity = this;

        /* set up the webview */
        mWebView = (WebView)findViewById(R.id.webview_main);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                activity.setProgress(newProgress * 1000);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                /* make links open in the webview */
                return true;
            }
        });

        /* apply some webview settings */
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        /* load the URL if the saved state is null */
        if (savedInstanceState == null) {
            /* retrieve the URL from the preferences */
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String serverUrl = prefs.getString(getString(R.string.pref_key_server_url), "");

            Log.d("app", "Server URL from preferences: " + serverUrl);

            /* check if the URL is not empty */
            if (!serverUrl.equalsIgnoreCase("")) {  //TODO check if a valid URL was retrieved
                mWebView.loadUrl(serverUrl);
            } else {
                /* display a warning message with a shortcut to the settings */
                Snackbar sb = Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.server_url_invalid), Snackbar.LENGTH_INDEFINITE);
                sb.setAction(R.string.open_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent settingsIntent = new Intent(WebViewActivity.this, SettingsActivity.class);  //TODO go directly to the general fragment
                        WebViewActivity.this.startActivity(settingsIntent);
                    }
                });
                sb.show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /* save the webview state */
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        /* load the webview state */
        mWebView.restoreState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                this.startActivity(settingsIntent);
                return true;
            case R.id.action_reload:
                mWebView.reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
