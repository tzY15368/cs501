package com.cs501.cs501app.buotg.user_setup

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Preview
@Composable
fun StuLinkImport(done: () -> Unit = {}) {
    // add the clickable modifier

    val timestamp = System.currentTimeMillis() / 1000
    var src =
        "https://www.bu.edu/link/bin/uiscgi_studentlink.pl/" + timestamp + "?ModuleName=allsched.pl"
    //src = "https://www.google.com"
    val (url, setUrl) = remember { mutableStateOf(src) }
    val (html, setHtml) = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        AndroidView(
            factory = { itOut ->
                WebView(itOut).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, newURL: String) {
                            // execute some javascript and get the result
                            val result =
                                view.evaluateJavascript("document.getElementsByTagName('html')[0].innerHTML.length") {
                                    Log.d("WebViewClient", "result web length: $it at $newURL")
                                }
                            // get the html
                            val html =
                                view.evaluateJavascript("document.getElementsByTagName('html')[0].innerHTML") {
                                    setHtml(it)
                                }
                            setUrl(newURL)
                            super.onPageFinished(view, newURL);
                        }
                    }
                    settings.javaScriptEnabled = true
                    loadUrl(src)
                }
            },
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Current URL: $url")
        // 10dp padding
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            Log.d("StuLinkImport", "html: ${html.length}")
            done()
        }, enabled = ("?ModuleName=allsched.pl" in url || "CURRENT SCHEDULE" in html)) {
            Text(text = "Import")
        }
    }
}

