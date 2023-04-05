package com.cs501.cs501app.example

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.cs501.cs501app.R
import kotlinx.coroutines.*


class WebViewDemo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_demo)
        val mWebview = findViewById<WebView>(R.id.webView)
        mWebview.settings.javaScriptEnabled = true
        // register a callback after a page has loaded
        mWebview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // do your stuff here
                // execute some javascript and get the result
                val result = view.evaluateJavascript("document.getElementsByTagName('html')[0].innerHTML.length") {
                    Log.d(url, "result web: $it")
                }
                super.onPageFinished(view, url);
            }
        }
        // get current timestamp in seconds
        val timestamp = System.currentTimeMillis() / 1000
        val url = "https://www.bu.edu/link/bin/uiscgi_studentlink.pl/" + timestamp + "?ModuleName=allsched.pl"

        mWebview.loadUrl(url)
         //launch a new coroutine
         CoroutineScope(Dispatchers.IO).launch {
             // do some background work
             val result = withContext(Dispatchers.IO) {
                 // do some background work
                 delay(1000)
             }
             // update the UI
             withContext(Dispatchers.Main) {
                 Log.d("===","result: $result")
             }
         }

    }
}