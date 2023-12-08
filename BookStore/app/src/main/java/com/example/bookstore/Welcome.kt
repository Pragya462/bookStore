package com.example.bookstore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        supportActionBar?.hide()

        val signupBtn = findViewById<Button>(R.id.signupBtn)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        signupBtn.setOnClickListener{
            intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}