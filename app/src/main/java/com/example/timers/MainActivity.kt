package com.example.timers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timers.ForegroundService.Companion.COMMAND_ID
import com.example.timers.ForegroundService.Companion.COMMAND_START
import com.example.timers.ForegroundService.Companion.COMMAND_STOP
import com.example.timers.ForegroundService.Companion.STARTED_TIMER_TIME_MS
import com.example.timers.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), ITimerListener, LifecycleObserver {

    private lateinit var binding: ActivityMainBinding

    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()

    private var startTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = timerAdapter
            }

            newTimerButton.setOnClickListener {
                val minutes = minuteEdit.text.toString().toLongOrNull()
                if (minutes == null) {
                    Toast.makeText(applicationContext, R.string.enter_minutes, LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val seconds = secondEdit.text.toString().toLongOrNull()
                if (seconds == null) {
                    Toast.makeText(applicationContext, R.string.enter_seconds, LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                timers.add(Timer(minutes * 60 * 1000 + seconds * 1000))
                timerAdapter.submitList(timers.toList())
            }

            minuteEdit.setText(INITIAL_TIMER_MINUTES.toString().padStart(2, '0'))
            secondEdit.setText(INITIAL_TIMER_SECONDS.toString().padStart(2, '0'))
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun delete(timer: Timer) {
        timer.stop()
        timers.remove(timer)
        timerAdapter.submitList(timers.toList())
    }

    override fun replace(timer: Timer) {
        if (timer.isRunning) {
            timers.forEach {
                if (it.id != timer.id && it.isRunning) it.stop()
            }
        }

        timerAdapter.notifyDataSetChanged()
        timerAdapter.submitList(timers.toList())
    }

    private fun getCurrentTime(): Long {
        timers.forEach {
            if (it.isRunning) return it.progress * 1000
        }
        return -1
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        val startIntent = Intent(this, ForegroundService::class.java)
        startIntent.putExtra(COMMAND_ID, COMMAND_START)
        startIntent.putExtra(STARTED_TIMER_TIME_MS, getCurrentTime())
        startService(startIntent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    private companion object {
        private const val INITIAL_TIMER_MINUTES = 0
        private const val INITIAL_TIMER_SECONDS = 30
    }

}
