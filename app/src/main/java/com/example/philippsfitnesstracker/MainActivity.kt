package com.example.philippsfitnesstracker


import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager?= null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var stepszero = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor==null){
            Toast.makeText(this,"No sensor detected", Toast.LENGTH_SHORT).show()
        } else{
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps: Int = totalSteps.toInt() - previousTotalSteps.toInt()
            tv_steps_taken.text = ("$currentSteps")

            progress_circular.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }
        }
    }

    private fun resetSteps(){
        tv_steps_taken.setOnClickListener{
            Toast.makeText(this,"Long tap to resest steps", Toast.LENGTH_SHORT).show()
        }
        tv_steps_taken.setOnLongClickListener{
            previousTotalSteps=totalSteps
            tv_steps_taken.text = 0.toString()
            saveData()
            true
        }
    }

    private fun saveData(){
        val sharedPreferences: SharedPreferences =getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat("key1",previousTotalSteps)
        editor.apply()
    }
    private fun loadData(){
        val sharedPreferences: SharedPreferences =getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
        val savedNumber: Float = sharedPreferences.getFloat("key1",0f)
        Log.d("MainActivity", "$savedNumber")
        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }


}