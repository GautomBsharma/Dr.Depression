package com.mksolution.depressionreducer

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.mksolution.depressionreducer.databinding.ActivityBreathOneBinding

class BreathOneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreathOneBinding
    private var selectedTime: String = "5"
    private var timerDuration: Long = 5 * 60000L // Default to 5 minutes
    private var mediaPlayer: MediaPlayer? = null
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val options = arrayOf("5", "10", "15", "20", "25", "30")

        binding.editTime.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Time")
                .setItems(options) { _, which ->
                    selectedTime = options[which]
                    binding.timerTextView.text = selectedTime
                    timerDuration = selectedTime.toLong() * 60000 // Update timer duration
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.checkBoxMale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkBoxFemale.isChecked = false
            }
        }

        binding.checkBoxFemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkBoxMale.isChecked = false
            }
        }

        binding.startButton.setOnClickListener {
            // Create the CountDownTimer with the updated timerDuration
            countDownTimer = object : CountDownTimer(timerDuration, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    val minutesRemaining = millisUntilFinished / 1000 / 60
                    val secondsRemaining = (millisUntilFinished / 1000) % 60
                    val timeFormatted = String.format("%02d:%02d", minutesRemaining, secondsRemaining)
                    binding.timerTextView.text = timeFormatted
                }

                override fun onFinish() {
                    binding.timerTextView.text = "Time's up!"
                    binding.animationBreath.cancelAnimation()
                    mediaPlayer?.stop()
                    mediaPlayer?.reset()
                    binding.startButton.visibility = View.VISIBLE
                    binding.stopButton.visibility = View.GONE
                }
            }

            // Start the countdown timer
            countDownTimer?.start()
            binding.animationBreath.playAnimation()

            // Initialize MediaPlayer when starting the animation
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@BreathOneActivity, Uri.parse("android.resource://" + packageName + "/" + R.raw.love_ringtone))
                prepare()
                start()
            }

            binding.startButton.visibility = View.GONE
            binding.stopButton.visibility = View.VISIBLE
        }

        binding.stopButton.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset() // Reset the media player to make it reusable
            }
            countDownTimer?.cancel()
            binding.animationBreath.cancelAnimation()
            binding.stopButton.visibility = View.GONE
            binding.startButton.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // Release media player resources when the activity is destroyed
        mediaPlayer = null
    }
}