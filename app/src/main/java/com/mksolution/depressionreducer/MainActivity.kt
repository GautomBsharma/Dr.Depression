package com.mksolution.depressionreducer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.mksolution.depressionreducer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var count = 0
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        //binding.progressBar.max = 60

        // Set initial progress
        val imageList = ArrayList<SlideModel>()
        val defaultImage = SlideModel(R.drawable.slider3, ScaleTypes.FIT)
        imageList.add(defaultImage)
        imageList.add(SlideModel(R.drawable.slider1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider4, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider5, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider6, ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)

        val familyImageList = ArrayList<SlideModel>()
        val defaultImageFamily = SlideModel(R.drawable.family2, ScaleTypes.FIT)
        familyImageList.add(defaultImageFamily)

        familyImageList.add(SlideModel(R.drawable.family1, ScaleTypes.FIT))
        familyImageList.add(SlideModel(R.drawable.family3, ScaleTypes.FIT))
        familyImageList.add(SlideModel(R.drawable.family4, ScaleTypes.FIT))
        familyImageList.add(SlideModel(R.drawable.family5, ScaleTypes.FIT))
        familyImageList.add(SlideModel(R.drawable.family6, ScaleTypes.FIT))
        familyImageList.add(SlideModel(R.drawable.family7, ScaleTypes.FIT))

        binding.imageSliderParents.setImageList(familyImageList, ScaleTypes.FIT)
        binding.tvGoMusic.setOnClickListener {
            startActivity(Intent(this,MusicActivity::class.java))
        }
        binding.editParent.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                startActivity(Intent(this, UpdateParentActivity::class.java))
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
            }

        }

        // Set click listener for the button
        /*binding.imIncriment.setOnClickListener {
            countt()
        }*/
    }
    /*private fun countt() {
        count +=10

        binding.progressBar.progress = count   // Update progress bar
        binding.textView3.text = count.toString()

        // Disable the button when the count reaches 20
        if (count >= 60) {
            count = 0
            binding.progressBar.progress = count
            binding.textView3.text = count.toString()
        }
    }*/


}