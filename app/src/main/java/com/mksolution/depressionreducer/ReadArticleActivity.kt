package com.mksolution.depressionreducer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.mksolution.depressionreducer.databinding.ActivityReadArticleBinding

class ReadArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arurl = intent.getStringExtra("ARTICLE_URL").toString()
        val title = intent.getStringExtra("ARTICLE_TITLE").toString()
        val description = intent.getStringExtra("ARTICLE_DES").toString()

        binding.backReadArticle.setOnClickListener {
            finish()
        }
        binding.titleAr.text = title
        binding.articleDescrip.text = description

        if (arurl.isNotEmpty()){
            Glide.with(this).load(arurl).into(binding.imageView4)
        }
        else{
            binding.imageView4.setImageResource(R.drawable.articlehint)
        }
    }
}