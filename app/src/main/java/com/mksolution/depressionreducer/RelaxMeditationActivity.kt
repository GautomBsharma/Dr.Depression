package com.mksolution.depressionreducer

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import com.mksolution.depressionreducer.databinding.ActivityRelaxMeditationBinding

class RelaxMeditationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRelaxMeditationBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var timeCounter: CountDownTimer? = null
    private var timeInSeconds = 0L
    private lateinit var audioManager: AudioManager
    private var maxVolume: Int = 0
    private var currentVolume: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRelaxMeditationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mediaPlayer = MediaPlayer.create(this, R.raw.calmandpeaceful)

        // Set up button click listeners
        binding.startButton.setOnClickListener {
            startMeditation()
        }

        binding.stopButton.setOnClickListener {
            stopMeditation()
        }
        binding.backRelaxMedi.setOnClickListener {
            finish()
        }
        binding.roleMeditation.setOnClickListener {
            //startActivity(Intent(this,MeditationRuleActivity::class.java))
        }
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        // Set up SeekBar for controlling volume
        binding.seekBar.max = maxVolume
        binding.seekBar.progress = currentVolume

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Set device volume as per the SeekBar progress
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No action needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No action needed
            }
        })
    }
    private fun startMeditation() {
        binding.animationMaditation.playAnimation()
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.start()
        }
        startTimer()
        binding.startButton.visibility = View.GONE
        binding.stopButton.visibility = View.VISIBLE
    }

    private fun stopMeditation() {
        binding.animationMaditation.pauseAnimation()

        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }

        // Stop timer
        timeCounter?.cancel()
        timeInSeconds = 0L
        updateTimerTextView(timeInSeconds)

        // Toggle buttons visibility
        binding.stopButton.visibility = View.GONE
        binding.startButton.visibility = View.VISIBLE
    }

    private fun startTimer() {
        timeCounter = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeInSeconds++
                updateTimerTextView(timeInSeconds)
            }

            override fun onFinish() {
                // Called when the timer finishes
            }
        }.start()
    }

    private fun updateTimerTextView(timeInSeconds: Long) {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        binding.timerTextView.text = String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release media player when activity is destroyed
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}