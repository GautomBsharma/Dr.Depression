package com.mksolution.depressionreducer.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mksolution.depressionreducer.Model.Music
import com.mksolution.depressionreducer.Model.formatDuration
import com.mksolution.depressionreducer.PlayerActivity
import com.mksolution.depressionreducer.R
import com.mksolution.depressionreducer.databinding.MusicViewBinding


class MusicAdapter(private val context: Context, private var musicList: ArrayList<Music>)
    : RecyclerView.Adapter<MusicAdapter.MyHolder>() {


    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageMV
        val duration = binding.songDuration
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(holder.image)

        holder.root.setOnClickListener {
            val intent = Intent(context,PlayerActivity::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","MusicAdapter")
            ContextCompat.startActivity(context,intent,null)

        }

    }
    override fun getItemCount(): Int {
        return musicList.size
    }

}