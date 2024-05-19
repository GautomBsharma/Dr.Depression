package com.mksolution.depressionreducer

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.mksolution.depressionreducer.databinding.ActivitySignUpBinding
import java.util.*
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: Dialog
    private var fcmToken :String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(true)
        binding.sinBtn.setOnClickListener {
            validateData()
        }
        binding.goLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
    private fun validateData() {
        if (binding.sinName.text.toString().isEmpty()){
            binding.sinName.error = "Enter Name"
        }
        else if (binding.sinEmail.text.toString().isEmpty()){
            binding.sinEmail.error = "Enter Email A/C"
        }
        else if (binding.sinPassword.text!!.length<6){
            binding.sinPassword.error = "Enter 6 or more!"
        }

        else if (binding.sinPassword.text.toString().isEmpty()){
            binding.sinPassword.error = "Enter password"
        }
        else{
            creatAcount()
            dialog.show()
        }
    }
    private fun creatAcount() {
        val email = binding.sinEmail.text.toString()
        val password = binding.sinPassword.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {

                    //getToken()
                    saveUser()
                } else
                {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
    }
    /*private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken = task.result
                //saveUser()
            } else {
                Toast.makeText(
                    baseContext, "get token fail",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }*/

    private fun saveUser() {
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
        val userMap = HashMap<String, Any>()
        val UserId = auth.currentUser!!.uid
        userMap["UserImageUrl"] = ""
        userMap["UserId"] = UserId
        userMap["UserName"] = binding.sinName.text.toString().lowercase(Locale.ROOT)
        userMap["UserPhone"] = ""

        userMap["UserInstitute"] = ""
        userMap["UserEmail"] = binding.sinEmail.text.toString()
        userMap["UserDistrict"] = ""
        userRef.child(UserId).setValue(userMap).addOnCompleteListener {
            startActivity(Intent(this, MainActivity::class.java))
            dialog.dismiss()
            finish()
        }.addOnFailureListener {
            Toast.makeText(
                baseContext, "Save data fail",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }
    }
}