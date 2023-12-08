package com.example.bookstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class BookDetails : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        var bookId: String? = null
        if(intent != null){
            bookId = intent.getStringExtra("book_id")
        }else{
            Toast.makeText(this , "Some Error occurred " , Toast.LENGTH_SHORT).show()
        }

        val image = findViewById<ImageView>(R.id.image)
        val name = findViewById<TextView>(R.id.name_of_book)
        val price = findViewById<TextView>(R.id.price_of_books)
        val description = findViewById<TextView>(R.id.DescriptionOfBook)
        val seller_name = findViewById<TextView>(R.id.sellerName)
        val seller_number = findViewById<TextView>(R.id.Seller_number)


        dbRef.child("Books").child(bookId!!).get().addOnSuccessListener {
            if(it.exists())
            {
                val bimage = it.child("image").value
                Picasso.get().load(bimage.toString()).into(image)
                val pName = it.child("name").value.toString()
                val pPrice = it.child("price").value.toString()
                val pDesc = it.child("description").value.toString()
                val pSeller = it.child("username").value.toString()
                val pNum = it.child("userNumber").value.toString()

                name.text = pName
                price.append(" $pPrice")
                description.append("  $pDesc")
                seller_name.append("  $pSeller")
                seller_number.append("  $pNum")
            }
            else{
                Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
        }
    }

}