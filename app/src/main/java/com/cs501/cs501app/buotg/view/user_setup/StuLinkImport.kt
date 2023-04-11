package com.cs501.cs501app.buotg.view.user_setup

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

const val STATUS_OK = "REG"
val SEASONS = listOf("Fall", "Spring", "Summer")

suspend fun parseHTML(html: String, events: MutableList<Event>) {
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

        if (headerCells?.size != expectedHeader.size) {
            continue
        }
        Log.d("parseHTML", "found table")
        var currentSemester = ""
        val rows = table.select("tr").drop(1)
        for (row in rows) {
            if (row.text().isEmpty()) continue

            // calculate current semester from the first cell
            val firstCell = row.select("td:eq(0)").first()
            println("first cell: ${firstCell?.text()}")
            var isFirstRow = false
            if(firstCell?.text()?.contains("Graph") == true) {
                // this is the semester indicator
                currentSemester = firstCell.text().replace("Instructors Graph Buy Books","")
                isFirstRow = true
            }
            if (!row.text().contains(STATUS_OK)) continue
            if(row.text().contains("no reg activity")) continue
            // get the number of tds in the row
            val tds = row.select("td").size
            // println("tds in row: $tds")
            var classIdx = 0
            if(isFirstRow){
                classIdx = 1
            } else {
                classIdx = 0
            }
            assert(tds - classIdx == 13)
            val nameCell = row.select("td:eq(${classIdx})").first()?.text()!!
            val instructorCell = row.select("td:eq(${classIdx + 4})").first()?.text()!!
            val buildingCell = row.select("td:eq(${classIdx + 7})").first()?.text()!!
            val roomCell = row.select("td:eq(${classIdx + 8})").first()?.text()!!
            val daysCell = row.select("td:eq(${classIdx + 9})").first()?.text()!!
            val startCell = row.select("td:eq(${classIdx + 10})").first()?.text()!!
            val endCell = row.select("td:eq(${classIdx + 11})").first()?.text()!!

            println("row: ${row.text().length}, ${row.toString()}")
            val classCell = row.select("td:eq(${classIdx})").first()
            // println("classCell: ${classCell}")
            if (classCell != null && classCell.text() != "") {
                val classEvents = generateEvents(
                    currentSemester,
                    nameCell,
                    instructorCell,
                    buildingCell,
                    roomCell,
                    daysCell,
                    startCell,
                    endCell
                )
                for (event in classEvents) {
                    events.add(event)
                }
            }
        }
        break
    }
    Log.d("parseHTML", "endo fparse")
}

// semester string: Spring 2023
// name string: CAS CS 501
// instructor string: Mobile App Development Czik
// building string: CAS
// room string: 101
// days string: Tue,Thu
// start string: 3:30pm
// end string: 4:45pm
suspend fun generateEvents(semester:String, name:String, instructor:String, building:String, room:String, days:String, start:String, end:String):List<Event>{
    val events = ArrayList<Event>()
    val semesterFields = semester.split(" ")
    val season = semesterFields[0]
    val field2 = semesterFields[1]
    val year = if(field2.length != 4){
        semesterFields[2]
    } else {
        field2
    }
    // TODO: PARSE SEASON AND YEAR AND START AND END
    val dayFields = days.split(",")
    // get the datetime of now
    val time = Calendar.getInstance().time
    val created_by = AppRepository.get().userRepo().getCurrentUserID()
    for (day in dayFields){
        val event = Event(
            event_id = UUID.randomUUID(),
            event_name = name,
            latitude = 0,
            longitude = 0,
            start_time = time,
            end_time = time,
            repeat_mode = 7,
            priority = 1,
            desc = instructor,
            created_by = created_by,
            notify_time = 15,
        )
        events.add(event)
    }
    return events
}

@Composable
fun PreviewClasses(events: MutableList<Event>) {
    // return none if classes is empty else concat classes with \n
    val out = if (events.size == 0) "none" else events.joinToString(separator = "\n")
    Text(text = out)
}

@Preview
@Composable
fun StuLinkImport(done: () -> Unit = {}) {
    // add the clickable modifier
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val timestamp = System.currentTimeMillis() / 1000
    var src =
        "https://www.bu.edu/link/bin/uiscgi_studentlink.pl/" + timestamp + "?ModuleName=allsched.pl"
    //src = "https://www.google.com"
    val (url, setUrl) = remember { mutableStateOf(src) }
    val (html, setHtml) = remember { mutableStateOf("") }
    val (showConfirm, setShowConfirm) = remember { mutableStateOf(false) }
    val eventsState = remember { mutableListOf<Event>() }
    val classesState = remember { mutableListOf<String>() }
    val userRepo = AppRepository.get().userRepo()
    val currentUser = remember { mutableStateOf<User?>(null) }
    LaunchedEffect(key1 = Unit) {
        currentUser.value = withContext(Dispatchers.IO) {
            userRepo.getCurrentUser()
        }
    }
    if(currentUser.value == null) {
        Text(text = "Please log in first")
        return
    }
    if (showConfirm) {
        PreviewClasses(events = eventsState)
        // button row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    Log.d("StudentlinkImport", "ok")
                    AppRepository.get().getEventRepository().upsertAll(eventsState)
                    TAlert.success(ctx, "ok")
                    done()
                }
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
            coroutineScope.launch {
                Log.d("StuLinkImport", "html: ${html.length}")
                parseHTML(html, eventsState)
                setShowConfirm(true)
            }
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

