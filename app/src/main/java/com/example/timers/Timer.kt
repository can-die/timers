package com.example.timers

import android.os.CountDownTimer

class Timer (val initTime: Long, var isRunning: Boolean = false, val id: Int = nextId++,
             private var currentTime: Long = initTime) {

    private var countDown: CountDownTimer? = null
    var holder: ITimerViewHolder? = null
    val progress: Long
        get() = currentTime / 1000

    fun start() {
        if (!isRunning) {
            isRunning = true
            countDown = setCountDownTimer(currentTime)
            countDown?.start()

            holder?.drawTimerState(this)
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            countDown?.cancel()

            holder?.drawTimerState(this)
        }
    }

    fun reset() {
        isRunning = false
        currentTime = initTime
        countDown?.cancel()

        holder?.drawTimerState(this)
    }

    private fun setCountDownTimer(ms: Long): CountDownTimer {
        return object : CountDownTimer(ms, TICK) {

            override fun onTick(millisUntilFinished: Long) {
                currentTime = millisUntilFinished + 100L
                if (currentTime > initTime) currentTime = initTime
                holder?.setTimerText(getTimerText())
                holder?.drawTimerProgress(currentTime / 1000)
            }

            override fun onFinish() {
                currentTime = 0
                isRunning = false
                holder?.drawTimerState(this@Timer)
            }
        }
    }

    fun getTimerText(ms: Long = this.currentTime): String {
        if (ms < 0L) {
            return getTimerText(0)
        }
        val hour = ms / 1000 / 3600
        val min = ms / 1000 % 3600 / 60
        val sec = ms / 1000 % 60

        return "${hour.twoDigits()}:${min.twoDigits()}:${sec.twoDigits()}"
    }

    private fun Long.twoDigits(): String = this.toString().padStart(2, '0')

    private companion object {
        private const val TICK = 1000L
        private var nextId = 0
    }
}

interface ITimerViewHolder {
    fun setTimerText(text: String)
    fun drawTimerProgress(progress: Long)
    fun drawTimerState(timer: Timer)
}
