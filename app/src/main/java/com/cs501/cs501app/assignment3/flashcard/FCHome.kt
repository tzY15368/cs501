package com.cs501.cs501app.assignment3.flashcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


class FCHome : AppCompatActivity() {
    private val backend by viewModels<FCBackend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backend.generateTenProblems()
        setContent {
            MaterialTheme {
                FCMainScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FCMainScreen() {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Flash Card",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                FCProblem()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FCProblem(backend: FCBackend = this.backend) {
        var answer by rememberSaveable { mutableStateOf("") }
        var round by rememberSaveable { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Display the current round
            Text(
                text = "Round ${round + 1}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
            // Display the current score
            Text(
                text = "Score: ${backend.getCurrentScore()}/10",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Get the current problem
            var problem = backend.getCurrentProblem()
            // Display the problem index and the problem
            Text(
                text = "Problem ${problem.index + 1} of 10",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = problem.operand1 + " " + (if (problem.isPlus) "+" else "-") + " " + problem.operand2,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Input field for the answer
            OutlinedTextField(
                value = answer,
                onValueChange = {
                    answer = it
                    Log.d("FC", "Answer: $answer")
                    Log.d("FC", "it: $it")
                },
                label = { Text("Answer") },
                placeholder = { Text("Enter your answer here") },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 24.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            // Submit button
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (answer == "") {
                            Toast.makeText(
                                this@FCHome,
                                "Please enter your answer",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }
                        if (backend.checkAnswer(answer)) {
                            Log.d("FC", "Correct!")
                            Toast.makeText(
                                this@FCHome,
                                "Correct!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Log.d("FC", "Incorrect!")
                            Toast.makeText(
                                this@FCHome,
                                "Incorrect!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        answer = ""
                        if (backend.getCurrentIndex() == 10) {
                            // display the score
                            Log.d("FC", "Score: ${backend.getCurrentScore()}/10")
                            Toast.makeText(
                                this@FCHome,
                                "Score: ${backend.getCurrentScore()}/10",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    content = {
                        Text("Submit")
                    },
                    enabled = backend.getCurrentIndex() < 10
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        backend.generateTenProblems()
                        Log.d("FC", "Generate 10 Problems")
                        Log.d("FC", backend.hasNextProblem().toString())
                        Log.d("FC", backend.getCurrentIndex().toString())
                        round++
                        Log.d("FC", "Round: $round")
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




