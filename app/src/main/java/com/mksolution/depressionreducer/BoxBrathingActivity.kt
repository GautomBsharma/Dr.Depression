package com.mksolution.depressionreducer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.SeekBar
import com.mksolution.depressionreducer.databinding.ActivityBoxBrathingBinding

class BoxBrathingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoxBrathingBinding
    private val handler = Handler(Looper.getMainLooper())
    private val sectionColors = arrayOf(Color.GREEN, Color.YELLOW, Color.BLUE, Color.RED) // 4 sections
    private var currentSection = 0
    private var isAnimating = false
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var animator: ValueAnimator
    private lateinit var audioManager: AudioManager
    private var maxVolume: Int = 0
    private var currentVolume: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBoxBrathingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding.startButton.setOnClickListener {
            if (!isAnimating) {
                startBreathingAnimation()
                binding.startButton.isEnabled = false
                binding.stopButton.isEnabled = true
            }
        }

        binding.stopButton.setOnClickListener {
            stopBreathingAnimation()
        }
        binding.backBoxBreath.setOnClickListener {
            finish()
        }

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        binding.seekBar.max = maxVolume
        binding.seekBar.progress = currentVolume

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun startBreathingAnimation() {
        isAnimating = true
        currentSection = 0
        binding.progressBar.progress = 0
        changeProgressBarColor(sectionColors[currentSection])
        playSound(isHold = false) // Play inhale sound (copperbell)
        animateProgressBar(4000) // 4 seconds for inhale
    }

    private fun animateProgressBar(duration: Long) {
        if (!isAnimating) return

        animator = ValueAnimator.ofInt(0, 100)
        animator.duration = duration
        animator.addUpdateListener {
            binding.progressBar.progress = it.animatedValue as Int
        }
        animator.start()

        // Handle transition to the next section
        handler.postDelayed({
            currentSection++
            when (currentSection) {
                1 -> {
                    // Hold for 4 seconds
                    changeProgressBarColor(sectionColors[currentSection])
                    playSound(isHold = true) // Play hold sound (servantbell)
                    animateProgressBar(4000)
                }
                2 -> {
                    // Exhale for 4 seconds
                    changeProgressBarColor(sectionColors[currentSection])
                    playSound(isHold = false) // Play exhale sound (same as inhale, copperbell)
                    animateProgressBar(4000)
                }
                3 -> {
                    // Hold for 4 seconds again
                    changeProgressBarColor(sectionColors[currentSection])
                    playSound(isHold = true) // Play hold sound (servantbell)
                    animateProgressBar(4000)
                }
                else -> {
                    // Reset and restart the cycle
                    currentSection = 0
                    changeProgressBarColor(sectionColors[currentSection])
                    playSound(isHold = false) // Restart inhale sound (copperbell)
                    animateProgressBar(4000)
                }
            }
        }, duration)
    }

    private fun changeProgressBarColor(color: Int) {
        binding.progressBar.progressTintList = android.content.res.ColorStateList.valueOf(color)
    }

    private fun playSound(isHold: Boolean) {
        if (mediaPlayer == null) {
            mediaPlayer =
                MediaPlayer.create(this, if (isHold) R.raw.servantbell else R.raw.copperbell)
        } else {
            mediaPlayer?.reset()
            mediaPlayer =
                MediaPlayer.create(this, if (isHold) R.raw.servantbell else R.raw.copperbell)
        }

        mediaPlayer?.start() // Play the respective sound
    }

    private fun stopBreathingAnimation() {
        isAnimating = false
        if (::animator.isInitialized && animator.isRunning) {
            animator.cancel()
        }
        handler.removeCallbacksAndMessages(null)

        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
        }

        binding.startButton.isEnabled = true
        binding.stopButton.isEnabled = false
    }

    override fun onPause() {
        super.onPause()
        stopBreathingAnimation()
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}