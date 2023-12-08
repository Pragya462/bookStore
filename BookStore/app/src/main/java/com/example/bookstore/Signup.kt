package com.example.bookstore

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstore.databinding.ActivitySignupBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        var name= findViewById<TextInputEditText>(R.id.name)
        var email = findViewById<TextInputEditText>(R.id.newEmail)
        var password = findViewById<TextInputEditText>(R.id.newPassword)
        val number = findViewById<TextInputEditText>(R.id.phoneNumber)

        var loginText = findViewById<TextView>(R.id.loginText)

        var submit = findViewById<Button>(R.id.submitbtn)

        nameCheck()
        emailCheck()
        passwordCheck()
        numberCheck()
        loginText.setOnClickListener{
            intent = Intent(this, Login::class.java)
            finish()
            startActivity(intent)
        }


        submit.setOnClickListener{
            if( (name.text.toString().trim() == "") || (email.text.toString().trim() == "") || (password.text.toString().trim() == "")
                || (number.text.toString() == ""))
            {
                Toast.makeText(this, "Please enter all the credentials.", Toast.LENGTH_SHORT).show()
            }
            else {
                submit()
            }
        }
    }
    private fun  submitData()
    {
        var name= findViewById<TextInputEditText>(R.id.name)
        var email = findViewById<TextInputEditText>(R.id.newEmail)
        var password = findViewById<TextInputEditText>(R.id.newPassword)
        val number = findViewById<TextInputEditText>(R.id.phoneNumber)

        mAuth.createUserWithEmailAndPassword(email.text.toString().trim(), password.text.toString().trim())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    addUserToDatabase(name.text.toString().trim(), email.text.toString().trim(),
                        password.text.toString().trim(), number.text.toString().trim(), mAuth.currentUser?.uid!!)

                    Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()
                    name.text = null
                    email.text = null
                    password.text = null
                    number.text = null

                    intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, password: String, number: String, uid: String){
        dbRef = FirebaseDatabase.getInstance().getReference()
        dbRef.child("Users").child(uid).setValue(Users(name,email,password,number))
    }

    private fun nameCheck()
    {
        binding.name.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.nameContainer.helperText = validName()
            }
        }
    }

    private fun validName() : String?
    {
        var name = findViewById<TextInputEditText>(R.id.name)
        if(name.text.toString().trim() == "")
            return "It is a mandatory field"
        else
            return null
    }

    private fun numberCheck()
    {
        binding.phoneNumber.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.phoneNumberContainer.helperText = validPhoneNumber()
            }
        }
    }

    private fun validPhoneNumber() : String?
    {
        val number = findViewById<TextInputEditText>(R.id.phoneNumber)

        if(number.text.toString().trim().length < 10)
        {
            return "Must be of 10 digits"
        }
        if(number.text.toString().trim().length > 10)
        {
            return "Should not exceed 10 digits"
        }
        return null

    }

    private fun emailCheck()
    {
        binding.newEmail.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.emailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.newEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordCheck()
    {
        binding.newPassword.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.newPassword.text.toString()
        if(passwordText.length < 8)
        {
            return "Minimum 8 Character Password"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Must Contain 1 Upper-case Character"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Must Contain 1 Lower-case Character"
        }
        if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
        {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }

        return null
    }

    private fun submit()
    {
        binding.nameContainer.helperText = validName()
        binding.passwordContainer.helperText = validPassword()
        binding.phoneNumberContainer.helperText = validPhoneNumber()

        val validName = binding.nameContainer.helperText == null
        val validPhoneNumber = binding.phoneNumberContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null

        if (validName && validPassword && validPhoneNumber)
            submitData()
        else
            Toast.makeText(this, "Please check the credentials again", Toast.LENGTH_LONG).show()
    }

}