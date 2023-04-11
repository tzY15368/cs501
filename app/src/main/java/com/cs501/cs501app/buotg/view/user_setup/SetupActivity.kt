package com.cs501.cs501app.buotg.view.user_setup

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.buotg.HomeActivity
import com.cs501.cs501app.utils.GenericTopAppBar


class SetupActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RenderScaffold()
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RenderScaffold() {
        val steps2 = listOf("Login/Register", "Import from studentlink","Whatelse?")
        val (step, setStep) = remember { mutableStateOf(0) }

        val title = "Setup" + " " + (step + 1) + "/" + steps2.size + ": " + steps2[step]

        // callback for when a step is done
        val stepDone: () -> Unit = {
            Log.d("SetupActivity", "stepDone: step = $step, steps2.size = ${steps2.size}")
            if (step < steps2.size - 1) {
                setStep(step + 1)
            }
        }
        Scaffold(
            topBar = {
                GenericTopAppBar(title = title)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                // render the current step
                when (step) {
                    0 -> LoginRegister(stepDone,"1")
                    1 -> StuLinkImport(stepDone)
                    2 -> Button(onClick = {
                        // start a new intent that goes to HomeActivity
                        val intent = Intent(this@SetupActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }) {
                        Text(text = "Go to Home Screen")
                    }
                }
                // spacer to keep things to the bottom
                // https://stackoverflow.com/questions/70904979/how-align-to-bottom-a-row-in-jetpack-compose
                Spacer(modifier = Modifier.weight(1f))
                // row of buttons, sticks to the bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // back button, disabled if on first step
                    Button(
                        onClick = { setStep((step - 1) % steps2.size) },
                        enabled = step > 0
                    ) {
                        Text(text = "<")
                    }
                    // next button, disabled if on last step
                    Button(
                        onClick = { setStep((step + 1) % steps2.size) },
                        enabled = step < steps2.size - 1
                    ) {
                        Text(text = ">")
                    }
                }
            }
        }
    }

}