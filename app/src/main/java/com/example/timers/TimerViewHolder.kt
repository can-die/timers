package com.example.timers

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.timers.databinding.TimersItemBinding

class TimerViewHolder(
        private val binding: TimersItemBinding,
        private val listener: ITimerListener
): RecyclerView.ViewHolder(binding.root), ITimerViewHolder {

        private var currentTimer: Timer? = null

        fun bind(timer: Timer) {

            if (currentTimer != timer) {
                currentTimer?.holder = null
                currentTimer = timer
                timer.holder = this
            }

            binding.customView.setPeriod(timer.initTime / 1000)
            binding.customView.setCurrent(timer.progress)

            drawTimerState(timer)

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

        override fun drawTimerState(timer: Timer) {
            if (timer.isRunning) {
                val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_pause_24)
                binding.startPauseButton.setImageDrawable(drawable)
                binding.blinkingIndicator.isInvisible = false
                (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
            } else {
                val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_play_arrow_24)
                binding.startPauseButton.setImageDrawable(drawable)
                binding.blinkingIndicator.isInvisible = true
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
            }

            setTimerText(timer.getTimerText())
            drawTimerProgress(timer.progress)

            if (timer.progress == 0L) {
                itemView.setBackgroundColor(itemView.resources.getColor(R.color.indigo_A100, null))
            } else {
                itemView.setBackgroundColor(Color.WHITE)
            }
        }

        override fun setTimerText(text: String) {
            binding.time.text = text
        }

        override fun drawTimerProgress(progress: Long) {
            binding.customView.setCurrent(progress)
       }

}