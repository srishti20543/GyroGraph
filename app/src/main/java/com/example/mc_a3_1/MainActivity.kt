package com.example.mc_a3_1

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mc_a3_1.ui.theme.MC_A3_1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var a by mutableFloatStateOf(0f)
    private var b by mutableFloatStateOf(0f)
    private var c by mutableFloatStateOf(0f)
    private val interval = 1000L
    private var elapsedTime = 0L
    private var angleEntries by mutableStateOf<List<Angles>>(emptyList())
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MC_A3_1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    setup()
                    startCollectingData()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "ACCELEROMETER VALUES",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Modifier.padding(16.dp)

                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Pitch: $a m/s\u00B2",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Modifier.padding(8.dp)
                                Text(
                                    text = "Roll: $b m/s\u00B2",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Modifier.padding(8.dp)
                                Text(
                                    text = "Yaw: $c m/s\u00B2",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, GraphActivity::class.java)
                                intent.putExtra("Angles", ArrayList(angleEntries))
                                startActivity(intent)

                            }
                        ) {
                            Text("View Graphs")
                        }
                    }
                }
            }
        }
    }

    private fun setup() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (event.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                a = event.values[0].round()
                b = event.values[1].round()
                c = event.values[2].round()
            }
        }
    }

    private fun startCollectingData() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (elapsedTime < 120000L) {
                delay(interval)
                elapsedTime += interval
                angleEntries += Angles((elapsedTime / 1000L).toInt(), a, b, c)
            }
            if (angleEntries.size >= 120) {
                angleEntries = emptyList()
                elapsedTime = 0L
            }

        }
    }

    private fun Float.round(): Float {
        return ((this * 100).roundToInt()).toFloat() / 100
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

