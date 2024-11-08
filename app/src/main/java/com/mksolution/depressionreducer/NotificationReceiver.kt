package com.mksolution.depressionreducer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mksolution.depressionreducer.Model.setSongPosition
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            //only play next or prev song, when music list contains more than one song
            //ApplicationClass.PREVIOUS -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = false, context = context!!)
            ApplicationClass.PLAY -> if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
            //ApplicationClass.NEXT -> if(PlayerActivity.musicListPA.size > 1) prevNextSong(increment = true, context = context!!)
            ApplicationClass.EXIT ->{
                //exitApplication()

                PlayerActivity.musicService?.stopForeground(true)
                PlayerActivity.musicService = null
                exitProcess(1)
            }
        }
    }
    private fun playMusic(){
        PlayerActivity.isPlaying = true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.round_pause_24)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.round_pause_24)
        //for handling app crash during notification play - pause btn (While app opened through intent)
        //try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.pause_icon) }catch (_: Exception){}
    }

    private fun pauseMusic(){
        PlayerActivity.isPlaying = false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.round_play_arrow_24)
        PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.round_play_arrow_24)
        //for handling app crash during notification play - pause btn (While app opened through intent)
        //try{ NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.play_icon) }catch (_: Exception){}
    }


    private fun prevNextSong(increment: Boolean, context: Context){
        setSongPosition(increment = increment)
        PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
        //PlayerActivity.musicService.mediaPlayer.prepare()
        Glide.with(context)
            .load(PlayerActivity.musicListPA[PlayerActivity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(PlayerActivity.binding.songImgPA)
        PlayerActivity.binding.songNamePA.text = PlayerActivity.musicListPA[PlayerActivity.songPosition].title

    }
}