package com.example.timers

import android.os.CountDownTimer

class Timer (private val initTime: Long, var isRunning: Boolean = false, val id: Int = nextId++, var currentTime: Long = initTime) {

    private var countDown: CountDownTimer? = null
    var holder: ITimerViewHolder? = null

    fun copy(): Timer {
        return Timer(initTime, isRunning, id, currentTime)
    }

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
                currentTime = millisUntilFinished
                holder?.setTimerText(getTimerText())
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

        return "${displaySlot(hour)}:${displaySlot(min)}:${displaySlot(sec)}"
    }

    private fun displaySlot(count: Long) = if (count > 9) "$count" else "0$count"

    private companion object {
        private const val TICK = 100L
        private var nextId = 0
    }
}

interface ITimerViewHolder {
    fun setTimerText(text: String)
    fun drawTimerState(timer: Timer)
}
