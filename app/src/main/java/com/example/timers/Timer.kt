package com.example.timers

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper

class Timer (val initTime: Long, var isRunning: Boolean = false, val id: Int = nextId++,
             private var currentTime: Long = initTime) {

    private var countDown: CountDownTimer? = null

    val progress: Long
        get() = currentTime
    val text: String
        get() = getTimerText()

    var tickCallback: (() -> Unit)? = null
    var stateCallback: (() -> Unit)? = null

    fun start() {
        if (!isRunning) {
            isRunning = true

            countDown = setCountDownTimer(currentTime)
            countDown?.start()

            stateCallback?.invoke()
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            countDown?.cancel()

            stateCallback?.invoke()
        }
    }

    fun reset() {
        isRunning = false
        currentTime = initTime
        countDown?.cancel()

        stateCallback?.invoke()
    }

    private fun setCountDownTimer(ms: Long): CountDownTimer {
        return object : CountDownTimer(ms, TICK) {

            override fun onTick(millisUntilFinished: Long) {
                currentTime = millisUntilFinished
                if (currentTime > initTime) currentTime = initTime
                tickCallback?.invoke()
            }

            override fun onFinish() {
                currentTime = 0
                isRunning = false
                beep()
                stateCallback?.invoke()
            }
        }
    }

    private fun getTimerText(): String {
        val fakeMs = if (currentTime <= 0L) 0L else { if (currentTime + 1000 > initTime) initTime else currentTime + 1000 }

        val hour = fakeMs / 1000 / 3600
        val min = fakeMs / 1000 % 3600 / 60
        val sec = fakeMs / 1000 % 60

        return "${hour.twoDigits()}:${min.twoDigits()}:${sec.twoDigits()}"
    }

    private fun Long.twoDigits(): String = this.toString().padStart(2, '0')


    private fun beep()
    {
        val duration = 300
        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneG.startTone(ToneGenerator.TONE_PROP_BEEP2, duration)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            toneG.release()
        }, (duration + 50).toLong())
    }
    private companion object {
        private const val TICK = 100L
        private var nextId = 0
    }
}

