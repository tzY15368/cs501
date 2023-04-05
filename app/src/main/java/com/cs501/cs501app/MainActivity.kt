package com.cs501.cs501app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.assignment2.Calc1Activity
import com.cs501.cs501app.assignment2.Calc2Activity
import com.cs501.cs501app.assignment3.flashcard.FCLoginActivity
import com.cs501.cs501app.assignment3.geoquiz.GeoQuizActivity
import com.cs501.cs501app.assignment3.tempconv.TempConvActivity
import com.cs501.cs501app.assignment4.boggle.BoggleActivity
import com.cs501.cs501app.assignment4.cintent.CriminalIntentActivity
import com.cs501.cs501app.assignment4.hangman.HangManActivity
import com.cs501.cs501app.buotg.HomeActivity

class MainActivity : AppCompatActivity() {

    private val activities = listOf(
        HomeActivity::class,
        Calc1Activity::class,
        Calc2Activity::class,
        GeoQuizActivity::class,
        TempConvActivity::class,
        FCLoginActivity::class,
        CriminalIntentActivity::class,
        HangManActivity::class,
        BoggleActivity::class,
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        MainTopAppBar()
                    },
                    content = { innerPadding ->
                        // center the column
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .padding(16.dp)
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            activities.forEachIndexed { idx, _ ->
                                ActivityEntry(idx)
                            }
                        }
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainTopAppBar() {
        TopAppBar(
            title = {
                Text(
                    text = "BU On-the-go",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
        )
    }

    @Composable
    fun ActivityEntry(activityIdx: Int) {
        val activity = activities[activityIdx]
        val simpleName = activity.java.simpleName.replace("Activity", "")
        Button(
            onClick = {
                val intent = Intent(this, activity.java)
                startActivity(intent)
            }
        ) {
            Text(text = "Goto $simpleName")
        }
        Spacer(modifier = Modifier.padding(8.dp))
    }
}