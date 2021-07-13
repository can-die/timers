package com.example.timers

interface ITimerListener {
    fun replace(newTimer: Timer)
    fun delete(timer: Timer)
}