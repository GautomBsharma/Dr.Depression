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
import com.mksolution.depressionreducer.databinding.ActivityEqualBreathBinding

class EqualBreathActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEqualBreathBinding
    private val handler = Handler(Looper.getMainLooper())
    private val sectionColors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
    private var currentSection = 0
    private var isAnimating = false
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var animator: ValueAnimator
    private lateinit var audioManager: AudioManager
    private var maxVolume: Int = 0
    private var currentVolume: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEqualBreathBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // Start button click listener
        binding.startButton.setOnClickListener {
            if (!isAnimating) {
                startBreathingAnimation()
                binding.startButton.isEnabled = false
                binding.stopButton.isEnabled = true
            }
        }


        // Stop button click listener
        binding.stopButton.setOnClickListener {
            stopBreathingAnimation()
        }
        binding.backEqualBreath.setOnClickListener {
            finish()
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

    private fun startBreathingAnimation() {
        isAnimating = true
        currentSection = 0
        binding.progressBar.progress = 0
        changeProgressBarColor(sectionColors[currentSection])
        playSound()  // Play the sound at the start of the first section
        animateProgressBar()
    }

    private fun animateProgressBar() {
        if (!isAnimating) return

        animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 4000 // 4 seconds per section
        animator.addUpdateListener {
            binding.progressBar.progress = it.animatedValue as Int
        }
        animator.start()

        // Handle transition to the next section after 4 seconds
        handler.postDelayed({
            currentSection++
            if (currentSection < sectionColors.size) {
                changeProgressBarColor(sectionColors[currentSection])
                playSound()  // Play the sound at the start of each new section
                animateProgressBar() // Recursively animate the next section
            } else {
                currentSection = 0 // Reset to the first section
                changeProgressBarColor(sectionColors[currentSection])
                playSound()  // Play the sound again when the loop starts over
                animateProgressBar() // Loop animation
            }
        }, 4000)
    }

    private fun changeProgressBarColor(color: Int) {
        binding.progressBar.progressTintList = android.content.res.ColorStateList.valueOf(color)
    }

    private fun playSound() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.copperbell) // Initialize the MediaPlayer if null
        }

        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop() // Stop current playback if it's playing
                it.prepare() // Prepare the MediaPlayer for the next playback
            }
            it.start() // Play the sound
        }
    }

    private fun stopBreathingAnimation() {
        isAnimating = false
        if (::animator.isInitialized && animator.isRunning) {
            animator.cancel()
        }
        handler.removeCallbacksAndMessages(null) // Stop handler from looping

        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.prepare() // Prepare for reuse if needed
            }
        }

        binding.startButton.isEnabled = true
        binding.stopButton.isEnabled = false
    }

    override fun onPause() {
        super.onPause()
        stopBreathingAnimation() // Stop the animation and sound when the activity goes into the background
    }

    override fun onStop() {
        super.onStop()
        releaseMediaPlayer() // Release the media player when the activity stops
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer() // Ensure resources are freed when the activity is destroyed
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release() // Release MediaPlayer resources
        mediaPlayer = null // Set media player reference to null
    }
}