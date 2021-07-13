package com.example.timers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.timers.databinding.TimersItemBinding

class TimerAdapter(private val listener: ITimerListener): ListAdapter<Timer, TimerViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TimersItemBinding.inflate(layoutInflater, parent, false)
        return TimerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Timer>() {

            override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
                return oldItem.currentTime == newItem.currentTime &&
                        oldItem.isRunning == newItem.isRunning
            }

            override fun getChangePayload(oldItem: Timer, newItem: Timer) = Any()

        }
    }
}