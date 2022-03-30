package com.example.photograhdeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser


        Handler().postDelayed({
            if (user != null) {
                startActivity(Intent(this@MainActivity, BottomActivity::class.java))

            } else {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }, 2000)
    }
}