package io.paan.android_testapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.paan.android_sensor.EnergyMonitor
import io.paan.android_testapp.ui.theme.AndroidTestappTheme

class MainActivity : ComponentActivity() {
    private lateinit var energyMonitor: EnergyMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTestappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        try {
            energyMonitor = EnergyMonitor(
                applicationContext,
                // db id and key are set in app/src/main/res/values/config.xml
                getString(R.string.db_id),
                getString(R.string.db_key),
            )
        } catch (e: Exception) {
            Log.e("TESTAPP", e.toString())
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidTestappTheme {
        Greeting("Android")
    }
}