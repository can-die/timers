package com.example.timers

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.timers.databinding.TimersItemBinding

class TimerViewHolder(
        private val binding: TimersItemBinding,
        private val listener: ITimerListener
): RecyclerView.ViewHolder(binding.root) {

        private var currentTimer: Timer? = null

        fun bind(timer: Timer) {

            if (currentTimer != timer) {
                currentTimer?.tickCallback = null
                currentTimer?.stateCallback = null
                currentTimer = timer

                timer.tickCallback = {
                    setTimerText(timer)
                    setTimerProgress(timer)
                }
                timer.stateCallback = {
                    setTimerState(timer)
                }
            }

            binding.customView.setPeriod(timer.initTime)
            binding.customView.setCurrent(timer.progress)

            setTimerState(timer)

            initButtonsListeners(timer)
        }

        private fun initButtonsListeners(timer: Timer) {

            binding.startPauseButton.setOnClickListener {
                if (timer.isRunning) {
                    timer.stop()
                } else {
                    timer.start()
                }
                listener.replace(timer)
            }

            binding.resetButton.setOnClickListener {
                timer.reset()
                listener.replace(timer)
            }

            binding.deleteButton.setOnClickListener {
                listener.delete(timer)
            }
        }

        private fun setTimerState(timer: Timer) {
            if (timer.isRunning) {
/*
                val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_pause_24)
                binding.startPauseButton.setImageDrawable(drawable)
*/
                binding.startPauseButton.text = itemView.context.getString(R.string.stop)
                (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
                binding.blinkingIndicator.isInvisible = false
            } else {
/*
                val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_play_arrow_24)
                binding.startPauseButton.setImageDrawable(drawable)
*/
                binding.startPauseButton.text = itemView.context.getString(R.string.start)
                binding.blinkingIndicator.isInvisible = true
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
            }

            setTimerText(timer)
            setTimerProgress(timer)

            if (timer.progress == 0L) {
                itemView.setBackgroundColor(itemView.resources.getColor(R.color.blue_200, null))
            } else {
                itemView.setBackgroundColor(Color.WHITE)
            }
        }

        private fun setTimerText(timer: Timer) {
            binding.time.text = timer.text
        }

        private fun setTimerProgress(timer: Timer) {
            binding.customView.setCurrent(timer.progress)
       }

}