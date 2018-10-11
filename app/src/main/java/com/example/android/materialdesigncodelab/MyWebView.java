package com.example.android.materialdesigncodelab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class MyWebView extends WebView {

    @SuppressLint("SetJavaScriptEnabled")
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalFadingEdgeEnabled(true);

        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);
        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setVerticalScrollBarEnabled(false);
        getSettings().setAllowFileAccess(true);
        getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowContentAccess(true);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        //addJQueryJS();
        //setWebViewClient(new MyWebClient(mContext));
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    //region JQuery Code
    /*private void addJQueryJS() {
        String path = "file:///android_asset/JSLibraries/jquery.min.js"; //TODO PLACE FILE HERE
        String data = "{\"MethodName\":\"onJQueryJSLoaded\",\"MethodArguments\":{}}";
        String callBackToNative = " jsInterface.callNativeMethod('jstoobjc:" + data + "');";
        String script = "function includeJSFile()"
                + "{"
                + "function loadScript(url, callback)"
                + "{"
                + "var script = document.createElement('script');"
                + "script.type = 'text/javascript';"
                + "script.onload = function () {"
                + "callback();"
                + "};"
                + "script.src = url;"
                + "if(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0])"
                + "{"
                + "(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(script);"
                + "}"
                + "else { callback(); }"
                + "}"
                + "loadScript('" + path + "', function ()"
                + "{"
                + callBackToNative
                + "});"
                + "} ; includeJSFile();";
        loadUrl("javascript: " + script);
    }*/
    //endregion
}