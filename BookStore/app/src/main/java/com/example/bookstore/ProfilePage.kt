package com.example.bookstore

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ProfilePage : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var imageUri: Uri
    private lateinit var saveCurrentDate: String
    private lateinit var saveCurrentTime: String
    private lateinit var userImageKey: String
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        storage = FirebaseStorage.getInstance()

        var pic = findViewById<ImageView>(R.id.profilePic)
        var name = findViewById<TextView>(R.id.currentUserName)
        var email = findViewById<TextView>(R.id.currentUserEmail)
        var number = findViewById<TextView>(R.id.currentUserNumber)

        dbRef.child(mAuth.currentUser?.uid!!).get().addOnSuccessListener {
            if(it.exists())
            {
                val pImage = it.child("profile_pic").value
                Picasso.get().load(pImage.toString()).placeholder(R.drawable.userpic).into(pic)
                val pName = it.child("name").value.toString()
                val pEmail = it.child("email").value.toString()
                val pNumber = it.child("number").value.toString()

                name.text = pName
                email.text = pEmail
                number.text = pNumber
            }
            else{
                Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
        }


        pic.setOnClickListener{
            openGallery()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var userImage = findViewById<ImageView>(R.id.profilePic)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            userImage.setImageURI(imageUri)
            storeImageInfo()
        }
    }

    private fun storeImageInfo() {
        val calender = Calendar.getInstance()

        val currentDate = SimpleDateFormat("MM dd, yyyy")
        saveCurrentDate = currentDate.format(calender.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calender.time)

        userImageKey = saveCurrentDate + saveCurrentTime

        uploadImage()

    }

    private fun uploadImage()
    {
        val reference = storage.reference.child("User_Images")
            .child(imageUri.lastPathSegment + Date().time.toString())
        reference.putFile(imageUri).addOnCompleteListener{
            if(it.isSuccessful)
            {
                reference.downloadUrl.addOnSuccessListener { task ->
                    addDataToDatabase(task.toString())
                }
            }
        }
    }

    private fun addDataToDatabase(userImage: String) {
        dbRef.child(mAuth.currentUser?.uid!!).child("profile_pic").setValue(userImage)
            .addOnSuccessListener {
                Toast.makeText(this, "Image updated successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
            }

    }
}