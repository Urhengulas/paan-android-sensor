package io.paan.energymonitor

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TAG: String = "ENERGY_MONITOR"

class EnergyMonitor(
    context: Context,
    dbId: String,
    dbToken: String,
    private val intervalMillis: Long = 1000,
    appName: String = "",
) {
    private val energyOfApp = EnergyOfApp(context)
    private val db = Database(dbId, dbToken)
    private val appName = if (appName == "") getApplicationName(context) else appName

    init {
        Log.d(TAG, "Initialized Energy Monitor")
        run()
    }

    private fun run() = CoroutineScope(Dispatchers.Default).launch {
        while (true) {
            try {
                // measure data
                val power = energyOfApp.energy()
                val time = energyOfApp.time()

                // send data to db
                val data = Energy(appName, power, time)
                db.insert(data)

                // sleep for intervalMs
                delay(intervalMillis)
            } catch (e: Exception) {
                Log.e(TAG, "$e")
            }
        }
    }
}

// Taken from https://stackoverflow.com/a/15114434
fun getApplicationName(context: Context): String {
    val applicationInfo = context.applicationInfo
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) {
        applicationInfo.nonLocalizedLabel.toString()
    } else {
        context.getString(stringId)
    }
}
