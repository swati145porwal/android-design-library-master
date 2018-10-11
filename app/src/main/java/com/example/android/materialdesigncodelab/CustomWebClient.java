package com.example.android.materialdesigncodelab;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by hp on 01-05-2017.
 */

public class CustomWebClient extends WebViewClient {
    private Context mContext;

    public CustomWebClient(Context context) {
        this.mContext = context;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        final MyWebView myWebView = (MyWebView) view;


       /* String varMySheet = "var mySheet = document.styleSheets[0];";

        String addCSSRule = "function addCSSRule(selector, newRule) {"
                + "ruleIndex = mySheet.cssRules.length;"
                + "mySheet.insertRule(selector + '{' + newRule + ';}', ruleIndex);"

                + "}";

        String insertRule1 = "addCSSRule('html', 'padding: 0px; height: "
                + (myWebView.getMeasuredHeight() /  mContext.getResources().getDisplayMetrics().density)
                + "px; -webkit-column-gap: 0px; -webkit-column-width: "
                + myWebView.getMeasuredWidth() + "px;')";

        myWebView.loadUrl("javascript:" + varMySheet);
        myWebView.loadUrl("javascript:" + addCSSRule);
        myWebView.loadUrl("javascript:" + insertRule1);*/

        String js = "javascript:function initialize() { " +
                "var d = document.getElementsByTagName('body')[0];" +
                "var ourH = window.innerHeight; " +
                "var ourW = window.innerWidth; " +
                "var fullH = d.offsetHeight; " +
                "var pageCount = Math.floor(fullH/ourH)+1;" +
                "var currentPage = 0; " +
                "var newW = pageCount*ourW; " +
                "d.style.height = ourH+'px';" +
                "d.style.width = newW+'px';" +
                "d.style.webkitColumnGap = '2px'; " +
                "d.style.margin = 0; " +
                "d.style.webkitColumnCount = pageCount;" +
                "}";
        myWebView.loadUrl(js);
        myWebView.loadUrl("javascript:initialize()");

    }

}
