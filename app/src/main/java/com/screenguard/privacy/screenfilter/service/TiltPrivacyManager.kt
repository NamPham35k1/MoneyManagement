package com.screenguard.privacy.screenfilter.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class TiltPrivacyManager(
    private val context: Context,
    private val onTiltChanged: (Boolean) -> Unit
) {
    private var sensorManager: SensorManager? = null
    private var sensorListener: SensorEventListener? = null

    private var lastShouldDim: Boolean? = null

    private val hysteresisBuffer = 3.0f

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
                tiltMode = TiltState.mode

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                // Tính toán góc nghiêng mượt hơn (bỏ nhân 2f để tránh quá nhạy)
                // Dùng atan2 giữa trục X và Z để đo độ nghiêng trái/phải chính xác hơn
                val angle = Math.toDegrees(atan2(x.toDouble(), z.toDouble())).toFloat()

                var shouldDim = false

                // Lấy trạng thái hiện tại (nếu chưa có thì mặc định là false)
                val currentDimState = lastShouldDim ?: false

                // Logic tính toán kèm khoảng đệm (Hysteresis)
                when (tiltMode) {
                    TiltMode.LEFT -> {
                        // Nếu đang tắt: cần nghiêng quá maxAngle để bật
                        // Nếu đang bật: cần quay về ít hơn (maxAngle - buffer) để tắt
                        shouldDim = if (!currentDimState) {
                            angle > maxAngle
                        } else {
                            angle > (maxAngle - hysteresisBuffer)
                        }
                    }
                    TiltMode.RIGHT -> {
                        shouldDim = if (!currentDimState) {
                            angle < -maxAngle
                        } else {
                            angle < -(maxAngle - hysteresisBuffer)
                        }
                    }
                    TiltMode.BOTH -> {
                        val absAngle = abs(angle)
                        shouldDim = if (!currentDimState) {
                            absAngle > maxAngle
                        } else {
                            absAngle > (maxAngle - hysteresisBuffer)
                        }
                    }
                }

                // CHỈ GỌI CALLBACK KHI TRẠNG THÁI THAY ĐỔI
                // Điều này triệt tiêu hoàn toàn hiện tượng "nháy" do gọi show/hide liên tục
                if (shouldDim != lastShouldDim) {
                    lastShouldDim = shouldDim
                    onTiltChanged(shouldDim)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager?.registerListener(
            sensorListener,
            sensor,
            SensorManager.SENSOR_DELAY_UI // Dùng DELAY_UI để giảm tải cho CPU và mượt hơn
        )
    }

    fun stop() {
        sensorListener?.let {
            sensorManager?.unregisterListener(it)
        }
        lastShouldDim = null // Reset trạng thái khi dừng
    }

    fun updateTiltMode(mode: TiltMode) {
        this.tiltMode = mode
    }
}