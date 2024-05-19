package com.mksolution.depressionreducer

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mksolution.depressionreducer.databinding.ActivityUpdateParentBinding
import java.io.*
import java.util.*

class UpdateParentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateParentBinding
    private lateinit var dialog: Dialog
    private lateinit var auth : FirebaseAuth
    private var imageUri : Uri?=null
    private var quote : String = ""

    private var launchGelaryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            imageUri = it.data!!.data
            binding.showImageParent.setImageURI(imageUri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateParentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        binding.pickImage.setOnClickListener {

            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGelaryActivity.launch(intent)
            binding.showImageParent.visibility = View.VISIBLE
        }
        binding.btnUp.setOnClickListener {
            validateData2()
        }

    }
    private fun validateData2() {
        if (imageUri.toString().isEmpty()){

            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show()
        }

        else{
            quote = binding.parentQuote.text.toString()
            uploadImages2(imageUri!!)
            dialog.show()
        }

    }
    private fun uploadImages2(uri: Uri) {
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("FamilyParentImage/$fileName")
        storageRef.putFile(uri).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {image->
                storeData2(image.toString())
            }
        }
            .addOnFailureListener{
                Toast.makeText(this, "Upload Storage Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
    }
    private fun storeData2(imageUrl: String) {
        val userId = auth.currentUser!!.uid
        val dbRef = FirebaseDatabase.getInstance().reference.child("FamilyParentSlider")
        val postId= dbRef.push().key
        val uplaodTime = System.currentTimeMillis()
        val postMap = HashMap<String, Any>()
        postMap["ParentSlideImageUrl"] = imageUrl
        postMap["UserId"] = userId
        postMap["uplaodTime"] = uplaodTime
        postMap["postId"] = postId.toString()
        postMap["title"] = binding.parentQuote.text.toString()
        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "post added", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

}