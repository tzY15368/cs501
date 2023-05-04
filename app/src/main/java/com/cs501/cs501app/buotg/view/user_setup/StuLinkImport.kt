package com.cs501.cs501app.buotg.view.user_setup

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
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
import com.cs501.cs501app.buotg.CustomButton
import com.cs501.cs501app.buotg.database.entities.Event
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.utils.TAlert
import kotlinx.coroutines.launch
import com.cs501.cs501app.buotg.database.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
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
            //println("first cell: ${firstCell?.text()}")
            var isFirstRow = false
            if (firstCell?.text()?.contains("Graph") == true) {
                // this is the semester indicator
                currentSemester = firstCell.text().replace("Instructors Graph Buy Books", "")
                isFirstRow = true
            }
            if (!row.text().contains(STATUS_OK)) continue
            if (row.text().contains("no reg activity")) continue
            // get the number of tds in the row
            val tds = row.select("td").size
            // println("tds in row: $tds")
            var classIdx = 0
            if (isFirstRow) {
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

            //println("row: ${row.text().length}, ${row.toString()}")
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

fun getLastDOWOfWeek(year: Int, month: Int, dow:Int): Calendar {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month)
    val lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    cal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth)
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
    val daysToSubtract = (dayOfWeek - dow + 7) % 7
    cal.add(Calendar.DAY_OF_MONTH, -daysToSubtract)
    return cal
}

fun getFirstDOWOfWeek(year: Int, month: Int, dow:Int): Calendar {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month)
    cal.set(Calendar.DAY_OF_MONTH, 1)
    while (cal.get(Calendar.DAY_OF_WEEK) != dow) {
        cal.add(Calendar.DAY_OF_MONTH, 1)
    }
    return cal
}

// semester string: Spring 2023
// name string: CAS CS 501
// instructor string: Mobile App Development Czik
// building string: CAS
// room string: 101
// days string: Tue,Thu
// start string: 3:30pm
// end string: 4:45pm
suspend fun generateEvents(
    semester: String,
    name: String,
    instructor: String,
    building: String,
    room: String,
    days: String,
    start: String,
    end: String
): List<Event> {
    val events = ArrayList<Event>()
    val semesterFields = semester.split(" ")
    val season = semesterFields[0]
    val field2 = semesterFields[1]
    val year = if (field2.length != 4) {
        semesterFields[2]
    } else {
        field2
    }
    // TODO: PARSE SEASON AND YEAR AND START AND END
    val dayFields = days.split(",")
    for (day in dayFields) {
        try {
            /*
            if season is spring, start from first day of Jan where the day is the same as the day in days,
            end at the last day of May where the day is the same as the day in days

            if season is summer, start from first day of July where the day is the same as the day in days,
            end at the last day of August where the day is the same as the day in days

            if season is fall, start from first day of September where the day is the same as the day in days,
            end at the last day of December where the day is the same as the day in days
             */
            var calStart = Calendar.getInstance()
            var calEnd = Calendar.getInstance()
            // set timezone to EST
            calStart.timeZone = TimeZone.getTimeZone("America/New_York")
            calEnd.timeZone = TimeZone.getTimeZone("America/New_York")
            // set the year
            val yy = year.toInt()
            calStart.set(Calendar.YEAR, yy)
            calEnd.set(Calendar.YEAR, yy)
            println("GOt season: $season, year: $year")
            if (season == "Spring") {
                calStart.set(Calendar.MONTH, Calendar.JANUARY)
                calEnd.set(Calendar.MONTH, Calendar.MAY)
            } else if (season == "Summer") {
                calStart.set(Calendar.MONTH, Calendar.JULY)
                calEnd.set(Calendar.MONTH, Calendar.AUGUST)
            } else if (season == "Fall") {
                calStart.set(Calendar.MONTH, Calendar.SEPTEMBER)
                calEnd.set(Calendar.MONTH, Calendar.DECEMBER)
            }
            // set the day of week
            val dayOfWeek = when (day) {
                "Mon" -> Calendar.MONDAY
                "Tue" -> Calendar.TUESDAY
                "Wed" -> Calendar.WEDNESDAY
                "Thu" -> Calendar.THURSDAY
                "Fri" -> Calendar.FRIDAY
                "Sat" -> Calendar.SATURDAY
                "Sun" -> Calendar.SUNDAY
                else -> Calendar.MONDAY
            }
            calStart = getFirstDOWOfWeek(yy, calStart.get(Calendar.MONTH), dayOfWeek)
            calEnd = getLastDOWOfWeek(yy, calEnd.get(Calendar.MONTH), dayOfWeek)
            //
            calEnd.set(Calendar.YEAR, yy)
            println("start: $start" + " end: $end"+ "end year: ${calEnd.get(Calendar.YEAR)}")
            // set the time
            val startFields = start.split(":")
            val endFields = end.split(":")
            var startHour = startFields[0].toInt()
            val startMinute = startFields[1].substring(0, 2).toInt()
            var endHour = endFields[0].toInt()
            val endMinute = endFields[1].substring(0, 2).toInt()

            if(start.contains("pm") && startHour != 12) {
                startHour += 12
            }
            if(end.contains("pm") && endHour != 12) {
                endHour += 12
            }

            calStart.set(Calendar.HOUR_OF_DAY, startHour)
            calStart.set(Calendar.MINUTE, startMinute)
            calEnd.set(Calendar.HOUR_OF_DAY, endHour)
            calEnd.set(Calendar.MINUTE, endMinute)
            calStart.set(Calendar.SECOND, 0)
            calStart.set(Calendar.MILLISECOND, 0)
            calStart.add(Calendar.SECOND, -calStart.get(Calendar.MILLISECOND))
            calEnd.set(Calendar.MILLISECOND, 0)
            calEnd.set(Calendar.SECOND, 0)
            calEnd.add(Calendar.SECOND, -calEnd.get(Calendar.MILLISECOND))
            val event = Event(
                event_id = UUID.randomUUID(),
                event_name = name,
                latitude = 0F,
                longitude = 0F,
                start_time = calStart.time,
                end_time = calEnd.time,
                repeat_mode = 7,
                priority = 1,
                desc = instructor,
                notify_time = 15,
            )
            events.add(event)
        } catch (e: Exception) {
            println("error: $e")
        }

    }
    return events
}

@Composable
fun PreviewClasses(events: MutableList<Event>) {
    // return none if classes is empty else concat classes with \n
    val out = if (events.size == 0) "none" else events.joinToString(separator = "\n\n")
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
    if (currentUser.value == null) {
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
            CustomButton(onClick = {
                coroutineScope.launch {
                    Log.d("StudentlinkImport", "ok")
                    Log.d("StudentlinkImport", "events: ${eventsState.size}")
                    for (event in eventsState) {
                        Log.d("StudentlinkImport", "event: ${event.event_name}")
                        AppRepository.get().eventRepo().upsertEvent(ctx, event, fromStuLink = true)
                    }
                    TAlert.success(ctx, "ok")
                    done()
                }
            }, text = "ok")
            CustomButton(onClick = {
                Log.d("StudentlinkImport", "cancel")
                TAlert.fail(ctx, "canceled, you may retry at any time")
                done()
            }, text = "cancel")
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
        CustomButton(
            {
                coroutineScope.launch {
                    Log.d("StuLinkImport", "html: ${html.length}")
                    parseHTML(html, eventsState)
                    setShowConfirm(true)
                }
            },
            text = "Import",
            enabled = ("?ModuleName=allsched.pl" in url || "CURRENT SCHEDULE" in html)
        )
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

