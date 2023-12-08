package com.example.bookstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper

class Login : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        var email = findViewById<TextInputEditText>(R.id.email)
        var password = findViewById<TextInputEditText>(R.id.password)

        var loginBtn = findViewById<Button>(R.id.loginbtn)
        var create = findViewById<TextView>(R.id.createacc)
        Paper.init(this);

        create.setOnClickListener{
            intent = Intent(this, Signup::class.java)
            finish()
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
            if( (email.text.toString().trim() == "") || (password.text.toString().trim() == ""))
            {
                Toast.makeText(this, "Please enter all the credentials.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                userLogin(email.text.toString(), password.text.toString())

            }
        }
    }

    private fun userLogin(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Logged in successfully.", Toast.LENGTH_SHORT).show()
                    intent = Intent(this,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }
}