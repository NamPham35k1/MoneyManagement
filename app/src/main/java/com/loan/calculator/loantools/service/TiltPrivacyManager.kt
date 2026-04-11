package com.loan.calculator.loantools.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.abs
import kotlin.math.atan2
class TiltPrivacyManager(
    private val context: Context,
    private val onTiltChanged: (Boolean) -> Unit
) {
    private var sensorManager: SensorManager? = null
    private var sensorListener: SensorEventListener? = null

    var maxAngle = 23f
    enum class TiltMode {
        LEFT, RIGHT, BOTH
    }

    var tiltMode = TiltMode.BOTH

    fun start() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorListener = object : SensorEventListener {


            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val g = Math.sqrt((x * x + y * y + z * z).toDouble())

                val angle = Math.toDegrees(
                    atan2(x.toDouble(), g)
                ).toFloat()*2f
                Log.d("maxAngle", "maxAngle: $maxAngle")

                var shouldDim = false

                when (tiltMode) {
                    TiltMode.LEFT -> {
                        if (angle > maxAngle) shouldDim = true
                    }
                    TiltMode.RIGHT -> {
                        if (angle < -maxAngle) shouldDim = true
                    }
                    TiltMode.BOTH -> {
                        if (abs(angle) > maxAngle) shouldDim = true
                    }
                }

                onTiltChanged(shouldDim)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager?.registerListener(
            sensorListener,
            sensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    fun stop() {
        sensorListener?.let {
            sensorManager?.unregisterListener(it)
        }
    }

}