package com.example.bookstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class myBookDetails : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_book_details)

        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        var bookId: String? = null
        if(intent != null){
            bookId = intent.getStringExtra("book_id")
        }else{
            Toast.makeText(this , "Some Error occurred " , Toast.LENGTH_SHORT).show()
        }

        val name = findViewById<TextView>(R.id.myBookName)
        val price = findViewById<TextView>(R.id.myBookPrice)
        val description = findViewById<TextView>(R.id.myBookDescription)
        val seller_name = findViewById<TextView>(R.id.myName)
        val seller_number = findViewById<TextView>(R.id.myNumber)

        var bookImg = findViewById<ImageView>(R.id.myBookImage)
        var soldBtn = findViewById<Button>(R.id.soldBtn)

        var setBtnText: String? = null

        dbRef.child("Books").child(bookId!!).get().addOnSuccessListener {
            if(it.exists())
            {
                val bimage = it.child("image").value
                Picasso.get().load(bimage.toString()).into(bookImg)
                val pName = it.child("name").value.toString()
                val pPrice = it.child("price").value.toString()
                val pDesc = it.child("description").value.toString()
                val pSeller = it.child("username").value.toString()
                val pNum = it.child("userNumber").value.toString()
                val pstatus = it.child("status").value.toString()

                name.text = pName
                price.append(" $pPrice")
                description.append("  $pDesc")
                seller_name.append("  $pSeller")
                seller_number.append("  $pNum")

                if(pstatus == "sold")
                    setBtnText = "MARK AS UNSOLD"
                else if(pstatus == "unsold")
                    setBtnText = "MARK AS SOLD"

                soldBtn.text = setBtnText
            }
            else{
                Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
        }

        soldBtn.setOnClickListener{
            if(soldBtn.text == "MARK AS SOLD")
            {
                dbRef.child("Books").child(bookId!!).child("status").setValue("sold")
                soldBtn.text = "MARK AS UNSOLD"
            }
            else if(soldBtn.text == "MARK AS UNSOLD")
            {
                dbRef.child("Books").child(bookId!!).child("status").setValue("unsold")
                soldBtn.text = "MARK AS SOLD"
            }
            Toast.makeText(this, "Status updated successfully", Toast.LENGTH_SHORT).show()
        }

    }
}