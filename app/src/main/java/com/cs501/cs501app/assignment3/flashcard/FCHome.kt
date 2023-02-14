package com.cs501.cs501app.assignment3.flashcard
import android.content.res.Configuration
import android.graphics.Bitmap.Config
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import com.cs501.cs501app.utils.TAlert
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


class FCHome : AppCompatActivity() {
    private val backend by viewModels<FCBackend>()
    private final val TAG = "FCHome"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FCMainScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FCMainScreen() {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Flash Card",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    modifier = Modifier.height(32.dp),
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    FCProblem(snackbarHostState)
                }
            },
        )
    }


    @Composable
    fun FCRoundState(round: Int, score: Int) {
        Column() {
            Text(
                modifier = Modifier.testTag("roundCnt"),
                text = "Round ${round + 1}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
            // Display the current score
            Text(
                text = "Score: ${score}/10",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    @Composable
    fun FCQuestionShow(problem: FCProblem) {
        // Display the problem index and the problem
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            val configuration = LocalConfiguration.current.orientation
            if (configuration == Configuration.ORIENTATION_PORTRAIT){
                Log.d(TAG, "direction: $configuration")
                Spacer(modifier = Modifier.height(100.dp))
            }
            Text(
                text = problem.operand1,
                modifier = Modifier.testTag("problemCnt"),
                //text = "Problem ${problem.index + 1} of 10",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    textAlign = TextAlign.Right
                )

            )
            Text(
                text = (if (problem.isPlus) "+ " else "- ") + problem.operand2,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    textAlign = TextAlign.Right
                )
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    // snackbarHoststate had to be passed in because only scaffolds can hold snackbars
    fun FCProblem(snackbarHostState: SnackbarHostState, backend: FCBackend = this.backend) {
        var answer by rememberSaveable { mutableStateOf("") }
        var round by rememberSaveable { mutableStateOf(0) }
        // val scope = rememberCoroutineScope()
        // Get the current problem
        val problem = backend.getCurrentProblem()
        val configuration = LocalConfiguration.current.orientation
        fun handleSubmit() {
            if (answer == "") {
                TAlert.fail(applicationContext, "Please enter your answer")
                return
            }
            if (backend.checkAnswer(answer)) {
                Log.d(TAG, "Correct!")
                TAlert.success(applicationContext,"Correct!")
            } else {
                Log.d(TAG, "Incorrect!")
                TAlert.fail(applicationContext, "Incorrect!")
            }
            answer = ""
            if (backend.getCurrentIndex() == 10) {
                // display the score
                Log.d(TAG, "Score: ${backend.getCurrentScore()}/10")
                TAlert.success(applicationContext, "Score: ${backend.getCurrentScore()} out of 10")
            }
        }

        fun handleGenerate() {
            backend.generateTenProblems()
            Log.d(TAG, "Generate 10 Problems")
            Log.d(TAG, backend.hasNextProblem().toString())
            Log.d(TAG, backend.getCurrentIndex().toString())
            round++
            Log.d(TAG, "Round: $round")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (configuration == Configuration.ORIENTATION_LANDSCAPE) Modifier.verticalScroll(
                        rememberScrollState()
                    ).padding(12.dp)
                    else Modifier.padding(16.dp)
                )

        ) {
            // Display the current round
            Row() {
                Column() {
                    FCRoundState(round = round, score = backend.getCurrentScore())
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Problem ${problem.index + 1} of 10",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                FCQuestionShow(problem = problem)
                Spacer(modifier = Modifier.height(16.dp))
            }
            // Input field for the answer
            OutlinedTextField(
                modifier = Modifier.testTag("answerField").align(if(configuration == Configuration.ORIENTATION_LANDSCAPE) Alignment.End else Alignment.Start),
                value = answer,
                maxLines = 1,
                singleLine = true,
                onValueChange = {
                    answer = it
                    Log.d(TAG, "Answer: $answer")
                    Log.d(TAG, "it: $it")
                },
                label = { Text("Answer") },
                placeholder = { Text("Enter your answer here") },
                textStyle = TextStyle(
                    fontSize = 24.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                enabled = backend.getCurrentIndex() != 10,
            )


            // Submit button
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier =  if (configuration == Configuration.ORIENTATION_LANDSCAPE) Modifier.align(Alignment.End)
                else Modifier.align(Alignment.Start),
            ) {
                Button(
                    modifier = Modifier.testTag("submitBtn"),
                    onClick = {
                        handleSubmit()
                    },
                    content = {
                        Text("Submit")
                    },
                    enabled = backend.getCurrentIndex() < 10
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier.testTag("generateBtn"),
                    onClick = {
                        handleGenerate()
                    },
                    content = {
                        Text("Generate 10 Problems")
                    },
                    // disable when the current index is not 10
                    enabled = backend.getCurrentIndex() == 10
                )
            }
        }
    }
}




