package eu.tutorials.stopwatchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.tutorials.stopwatchapp.ui.theme.StopwatchappTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StopwatchApp()
                }
            }
        }
    }
}

@Composable
fun StopwatchApp() {
    // Stopwatch state variables
    var timeInMillis by remember { mutableStateOf(0L) }   // Elapsed time in milliseconds
    var isRunning by remember { mutableStateOf(false) }   // Stopwatch running state

    val coroutineScope = rememberCoroutineScope()


    val buttonColor by animateColorAsState(
        targetValue = if (isRunning) Color.Red else Color.Green,
        animationSpec = tween(durationMillis = 500), label = ""  // Smooth transition for 500ms
    )


    // Function to start/resume the timer
    fun startTimer() {
        if (!isRunning) {
            isRunning = true
            coroutineScope.launch {
                while (isRunning) {
                    delay(10L) // Update every 10 milliseconds (for accuracy)
                    timeInMillis += 10L
                }
            }
        }
    }

    // Function to pause the timer
    fun pauseTimer() {
        isRunning = false
    }

    // Function to reset the timer
    fun resetTimer() {
        isRunning = false
        timeInMillis = 0L
    }

    // Convert milliseconds to minutes, seconds, and milliseconds
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val milliseconds = (timeInMillis % 1000) / 10

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display Timer Text
        Text(
            text = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(32.dp))


        Crossfade(targetState = isRunning, label = "") { running ->
            Button(
                onClick = { if (running) pauseTimer() else startTimer() },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(if (running) "Pause" else "Start")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset Button with custom color and animated disable effect
        Button(
            onClick = { resetTimer() },
            enabled = timeInMillis > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (timeInMillis > 0) Color.Gray else Color.LightGray
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Reset")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    StopwatchApp()
}



