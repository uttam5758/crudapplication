package com.example.crudapplication.ui.activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudapplication.R
import com.example.crudapplication.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.SignupButton.setOnClickListener{
            auth.createUserWithEmailAndPassword(binding.emailInput.text.toString(),
                binding.passwordInput.text.toString())
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Log.d(TAG,"createUserEmail:Success")
                        val user = auth.currentUser
                        val intent = Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        Log.w(TAG,"createWithEmail:Failure", it.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

}