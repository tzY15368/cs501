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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cs501.cs501app.utils.TAlert
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import java.util.regex.Pattern

fun parseHTML(html: String, classes: MutableList<String>) {
    val doc = Jsoup.parse(html)
    val expectedHeader = listOf(
        "Semester",
        "Class",
        "Status",
        "Cr",
        "Hrs",
        "Title/Instructor",
        "",
        "Type",
        "Bld",
        "Rm",
        "Day",
        "Start",
        "Stop",
        "Notes"
    )
    val tables = doc.select("table")
    for (table in tables) {
        val headerRow = table.select("tr").first()
        var headerCells = headerRow?.select("th")?.map { it.text() }
        // for each item in headerCells, remove the "\n" and space

        // println("table first row:" + headerCells?.size)
        // println("table std-- row:" + expectedHeader.size)
        if (headerCells?.size != expectedHeader.size) {
            continue
        }
        Log.d("parseHTML", "found table")
        val rows = table.select("tr").drop(1)
        for (row in rows) {
            if (row.text().isEmpty()) continue
            // get the number of tds in the row
            val tds = row.select("td").size
            // println("tds in row: $tds")
            var classIdx = 0
            if (tds == 14) {
                // this is the first row
                classIdx = 1
            } else if (tds == 13) {
                // a normal row
            } else {
                // something else
                continue
            }
            // println("row: ${row.text().length}, ${row.toString()}")
            val classCell = row.select("td:eq(${classIdx})").first()
            // println("classCell: ${classCell}")
            if (classCell != null && classCell.text() != "") {
                classes.add(classCell.text())
                // println("got class: ${classCell.text()}")
            }
        }
    }
    Log.d("parseHTML", "endo fparse")
}

@Composable
fun PreviewClasses(classes: MutableList<String>) {
    // return none if classes is empty else concat classes with \n
    val out = if (classes.size == 0) "none" else classes.joinToString(separator = "\n")
    Text(text = out)
}

@Preview
@Composable
fun StuLinkImport(done: () -> Unit = {}) {
    // add the clickable modifier
    val ctx = LocalContext.current
    val timestamp = System.currentTimeMillis() / 1000
    var src =
        "https://www.bu.edu/link/bin/uiscgi_studentlink.pl/" + timestamp + "?ModuleName=allsched.pl"
    //src = "https://www.google.com"
    val (url, setUrl) = remember { mutableStateOf(src) }
    val (html, setHtml) = remember { mutableStateOf("") }
    val (showConfirm, setShowConfirm) = remember { mutableStateOf(false) }
    val classesState = remember { mutableListOf<String>() }
    if (showConfirm) {
        PreviewClasses(classes = classesState)
        // button row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                Log.d("StudentlinkImport", "ok")
                TAlert.success(ctx, "ok")
                done()
            }) {
                Text(text = "ok")
            }
            Button(onClick = {
                Log.d("StudentlinkImport", "cancel")
                TAlert.fail(ctx, "canceled, you may retry at any time")
                done()
            }) {
                Text(text = "cancel")
            }
        }
        return
    }
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
                                    // unescape the html
                                    val it2 = unescapeUnicode(it)
                                    //val it2 = StringEscapeUtils.unescapeHtml4(html)
                                    setHtml(it2)
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
            parseHTML(html, classesState)
            setShowConfirm(true)
        }, enabled = ("?ModuleName=allsched.pl" in url || "CURRENT SCHEDULE" in html)) {
            Text(text = "Import")
        }
    }
}

fun unescapeUnicode(html: String): String {
    val pattern = Pattern.compile("\\\\u(\\p{XDigit}{4})")
    val matcher = pattern.matcher(html)
    val buffer = StringBuffer(html.length)

    while (matcher.find()) {
        val hexString = matcher.group(1)
        val charValue = hexString.toInt(16).toChar()
        matcher.appendReplacement(buffer, charValue.toString())
    }

    matcher.appendTail(buffer)
    return buffer.toString().replace("\\n", "")
}

