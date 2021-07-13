package com.example.timers

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timers.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), ITimerListener {

    private lateinit var binding: ActivityMainBinding

    private val timerAdapter = TimerAdapter(this)
    private val timers = mutableListOf<Timer>()

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
                timers.add(Timer(
                    minuteEdit.text.toString().toLong() * 60 * 1000 +
                            secondEdit.text.toString().toLong() * 1000
                ))
                timerAdapter.submitList(timers.toList())
            }

            minuteEdit.setText(INITIAL_TIMER_MINUTES.toString().padStart(2, '0'))
            secondEdit.setText(INITIAL_TIMER_SECONDS.toString().padStart(2, '0'))
        }

    }

    override fun delete(timer: Timer) {
        timer.stop()
        timers.remove(timer)
        timerAdapter.submitList(timers.toList())
    }

    override fun replace(newTimer: Timer) {
        val timer = timers.find { it.id == newTimer.id }
        val index = timers.indexOf(timer)
        timers.remove(timer)
        timers.add(index, newTimer)

        if (newTimer.isRunning) {
            timers.forEach {
                if (it.id != newTimer.id && it.isRunning) it.stop()
            }
        }

        timerAdapter.submitList(timers.toList())
    }

    private companion object {
        private const val INITIAL_TIMER_MINUTES = 15
        private const val INITIAL_TIMER_SECONDS = 0
    }

}
