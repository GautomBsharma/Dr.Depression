package com.mksolution.depressionreducer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mksolution.depressionreducer.Model.Music
import com.mksolution.depressionreducer.Model.setSongPosition
import com.mksolution.depressionreducer.Services.MusicService
import com.mksolution.depressionreducer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity(),ServiceConnection {


    companion object {
        lateinit var musicListPA : ArrayList<Music>
        var songPosition: Int = 0
        //var mediaPlayer : MediaPlayer ?= null
        var isPlaying:Boolean = false
        var musicService: MusicService? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlayerBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentService = Intent(this, MusicService::class.java)
        bindService(intentService, this, BIND_AUTO_CREATE)
        startService(intentService)
        initializeLayout()

        binding.playPauseBtnPA.setOnClickListener{
            if(isPlaying) {
                pauseMusic()
            }
            else{
                playMusic()
            }

        }
        binding.previousBtnPA.setOnClickListener { prevNextSong(increment = false) }
        binding.nextBtnPA.setOnClickListener { prevNextSong(increment = true) }

    }

    private fun prevNextSong(increment: Boolean) {
        if(increment)
        {
            setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()
        }
        else{
            setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }
    }


    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MusicActivity.MusicListMA)
                setLayout()
                //createMediaPlayer()
            }
        }
    }

    private fun setLayout() {
        Glide.with(applicationContext)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(binding.songImgPA)
        binding.songNamePA.text = musicListPA[songPosition].title
    }
    fun createMediaPlayer(){
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseBtnPA.setIconResource(R.drawable.round_pause_24)
            musicService!!.showNotification(R.drawable.round_pause_24)
        }catch (e: Exception){Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()}
    }

    private fun playMusic(){
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
        binding.playPauseBtnPA.setIconResource(R.drawable.round_pause_24)
        musicService!!.showNotification(R.drawable.round_pause_24)

    }
    private fun pauseMusic(){
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
        binding.playPauseBtnPA.setIconResource(R.drawable.round_play_arrow_24)
        musicService!!.showNotification(R.drawable.round_play_arrow_24)

    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }


}