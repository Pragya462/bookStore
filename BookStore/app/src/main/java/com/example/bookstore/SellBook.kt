package com.example.bookstore

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class SellBook : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var builder: AlertDialog.Builder
    private lateinit var imageUri: Uri
    private lateinit var saveCurrentDate: String
    private lateinit var saveCurrentTime: String
    private lateinit var bookRandomKey: String
    private lateinit var downloadedUrl: String
    private lateinit var loading: loadingClass2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_book)

        loading = loadingClass2(this)

        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        builder = AlertDialog.Builder(this)

        var bookImage = findViewById<ImageView>(R.id.bookImage)

        var inputBookName = findViewById<TextInputEditText>(R.id.bookName)
        var inputBookPrice = findViewById<TextInputEditText>(R.id.bookPrice)
        var inputBookDescription = findViewById<TextInputEditText>(R.id.bookDescription)
        var inputYourName = findViewById<TextInputEditText>(R.id.yourName)
        var inputYourContactNumber = findViewById<TextInputEditText>(R.id.yourContactNumber)

        var bookName = inputBookName.text.toString().trim()
        var bookPrice = inputBookPrice.text.toString().trim()
        var bookDescription = inputBookDescription.text.toString().trim()
        var yourName = inputYourName.text.toString().trim()
        var yourNumber = inputYourContactNumber.text.toString().trim()

        var sellBtn = findViewById<Button>(R.id.sellBtn)
        var backBtn = findViewById<ImageView>(R.id.backBtn)

        backBtn.setOnClickListener{
                builder.setTitle("Go back without selling?").setMessage("If you go back all the data in the entry fields will be lost.")
                    .setCancelable(true)
                    .setPositiveButton("Yes"){dialogInterface, it ->
                        finish()
                    }
                    .setNegativeButton("No"){dialogInterface, it ->
                        dialogInterface.cancel()
                    }.show()
        }

        sellBtn.setOnClickListener{
            loading.startLoading()
            validateData()
        }

        bookImage.setOnClickListener{
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
        var bookImage = findViewById<ImageView>(R.id.bookImage)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            bookImage.setImageURI(imageUri)
        }
    }

    private fun validateData()
    {
        var inputBookName = findViewById<TextInputEditText>(R.id.bookName)
        var inputBookPrice = findViewById<TextInputEditText>(R.id.bookPrice)
        var inputBookDescription = findViewById<TextInputEditText>(R.id.bookDescription)
        var inputYourName = findViewById<TextInputEditText>(R.id.yourName)
        var inputYourContactNumber = findViewById<TextInputEditText>(R.id.yourContactNumber)

        var bookName = inputBookName.text.toString().trim()
        var bookPrice = inputBookPrice.text.toString().trim()
        var bookDescription = inputBookDescription.text.toString().trim()
        var yourName = inputYourName.text.toString().trim()
        var yourNumber = inputYourContactNumber.text.toString().trim()

        if(imageUri == null)
        {
            loading.isDismiss()
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        }
        else if(bookName == "" || bookPrice == "" || bookDescription == "" || yourName == "" || yourNumber == "")
        {
            loading.isDismiss()
            Toast.makeText(this, "Please enter all the credentials...", Toast.LENGTH_SHORT).show()
        }
        else
        {
            uploadImage()
        }

    }
    private fun storeProductInfo() {
        val calender = Calendar.getInstance()

        val currentDate = SimpleDateFormat("MM dd, yyyy")
        saveCurrentDate = currentDate.format(calender.time)

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calender.time)

        bookRandomKey = saveCurrentDate + saveCurrentTime

    }

    private fun uploadImage()
    {
        storeProductInfo()
        val reference = storage.reference.child("books")
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

    private fun addDataToDatabase(imgUrl: String)
    {
        val inputBookName = findViewById<TextInputEditText>(R.id.bookName)
        val inputBookPrice = findViewById<TextInputEditText>(R.id.bookPrice)
        val inputBookDescription = findViewById<TextInputEditText>(R.id.bookDescription)
        val inputYourName = findViewById<TextInputEditText>(R.id.yourName)
        val inputYourContactNumber = findViewById<TextInputEditText>(R.id.yourContactNumber)

        val bookName = inputBookName.text.toString().trim()
        val bookPrice = inputBookPrice.text.toString().trim()
        val bookDescription = inputBookDescription.text.toString().trim()
        val yourName = inputYourName.text.toString().trim()
        val yourNumber = inputYourContactNumber.text.toString().trim()
        val uid = mAuth.currentUser?.uid!!

        dbRef = FirebaseDatabase.getInstance().getReference()
        dbRef.child("Books").child(bookRandomKey).setValue(books(bookRandomKey, imgUrl, bookName, bookPrice, bookDescription,
            yourName, yourNumber, uid, "unsold")).addOnSuccessListener {
                loading.isDismiss()
                Toast.makeText(this, "Book Uploaded Successfully", Toast.LENGTH_SHORT).show()
        }
    }
}