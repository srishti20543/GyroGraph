package com.example.mc_a3_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.mc_a3_1.ui.theme.MC_A3_1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GraphActivity : ComponentActivity() {

    private lateinit var anglesViewModel: AnglesViewModel
    private lateinit var anglesDatabase: AnglesDatabase
    private var angleEntries by mutableStateOf<List<Angles>>(emptyList())
    private var pitches by mutableStateOf<List<Float>>(emptyList())
    private var rolls by mutableStateOf<List<Float>>(emptyList())
    private var yaws by mutableStateOf<List<Float>>(emptyList())

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anglesDatabase = AnglesDatabase.getDatabase(applicationContext)
        anglesViewModel = AnglesViewModel(anglesDatabase.dao())

        val anglesList = intent.getSerializableExtra("Angles") as? ArrayList<Angles>
        if (anglesList != null) {
            angleEntries = anglesList.toMutableList()
            Log.d("size", angleEntries.size.toString())
            insertDataIntoDatabase()
        }

        setContent {
            MC_A3_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        lifecycleScope.launch {
                            anglesViewModel.angleEntries.collect { angleEntries ->

                                pitches = makeSingleList(angleEntries, "pitch")
                                rolls = makeSingleList(angleEntries, "roll")
                                yaws = makeSingleList(angleEntries, "yaw")

                            }
                        }

                        item {
                            Text(
                                "Graphs",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(16.dp)
                            )

                        }

                        item {
                            if (pitches.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(600.dp)
                                        .background(Color.LightGray)
                                        .padding(32.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LineGraph(data = pitches)
                                    Spacer(modifier = Modifier.height(16.dp))

                                }
                                Text("Pitches")
                            }
                        }

                        item {
                            if (rolls.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(600.dp)
                                        .background(Color.LightGray)
                                        .padding(32.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LineGraph(data = rolls)
                                    Spacer(modifier = Modifier.height(16.dp))

                                }
                                Text("Rolls")
                            }
                        }

                        item {

                            if (yaws.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(600.dp)
                                        .background(Color.LightGray)
                                        .padding(32.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LineGraph(data = yaws)
                                    Spacer(modifier = Modifier.height(16.dp))

                                }
                                Text("Yaws")
                            }
                        }


                        item {
                            Button(onClick = {
                                val intent = Intent(this@GraphActivity, MainActivity::class.java)
                                startActivity(intent)
                            }) {
                                Text("View Angles")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun insertDataIntoDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            for (item in angleEntries.listIterator()) {
                val angle = Angles(item.seconds, item.pitch, item.roll, item.yaw)
                anglesViewModel.insert(angle)
            }
        }
    }

    private fun makeSingleList(angleList: List<Angles>, type: String): List<Float> {
        val tempList = mutableListOf<Float>()

        for (angle in angleList) {
            when (type) {
                "pitch" -> tempList.add(angle.pitch)
                "roll" -> tempList.add(angle.roll)
                "yaw" -> tempList.add(angle.yaw)
            }
        }
        return tempList
    }
}

@Composable
fun LineGraph(data: List<Float>) {
    val maxValue = data.maxOrNull() ?: 1f
    val minValue = data.minOrNull() ?: 0f
    var graphWidth by remember { mutableIntStateOf(0) }
    var graphHeight by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .width(600.dp)
            .height(200.dp)
    ) {
        Canvas(
            modifier = Modifier
                .width(600.dp)
                .height(200.dp)
        ) {
            graphWidth = size.width.toInt()
            graphHeight = size.height.toInt()

            val stepX = graphWidth.toFloat() / (data.size - 1)
            val stepY = graphHeight.toFloat() / (maxValue - minValue)

            val path = Path()
            path.moveTo(0f, graphHeight.toFloat())

            data.forEachIndexed { index, value ->
                val x = index * stepX
                val y = graphHeight.toFloat() - ((value - minValue) * stepY)
                path.lineTo(x, y)
            }

            drawPath(
                path = path, color = Color.Blue, alpha = 1f, style = Stroke(width = 3.dp.toPx())
            )

            val xLabels = (0..data.size step 20).map { it.toString() }
            xLabels.forEachIndexed { index, label ->
                val x = (index * (graphWidth / (xLabels.size - 1))).toFloat()
                drawLine(
                    start = Offset(x, graphHeight.toFloat()),
                    end = Offset(x, graphHeight + 8.dp.toPx()),
                    color = Color.Black
                )
                drawIntoCanvas {
                    it.nativeCanvas.drawText(label,
                        x - 10.dp.toPx(),
                        graphHeight + 20.dp.toPx(),
                        Paint().apply {
                            color = Color.Black.toArgb()
                            textSize = 16.sp.toPx()
                        })
                }
            }
        }
    }
}