package com.example.timers

interface ITimerListener {
    fun replace(timer: Timer)
    fun delete(timer: Timer)
}