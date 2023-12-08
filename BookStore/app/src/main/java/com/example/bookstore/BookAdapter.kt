package com.example.bookstore

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class BookAdapter(val context: Context, private val bookList: ArrayList<books>): RecyclerView.Adapter<BookViewHolder>()
{

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.cards, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        val currentBook = bookList[position]
        var bid: String? = null
        val imageUri = currentBook.image

        Picasso.get().load(imageUri).into(holder.image)
        holder.title.text = currentBook.name
        holder.price.text = currentBook.price

        mAuth = FirebaseAuth.getInstance()

        bid = currentBook.bid.toString()
        holder.itemView.setOnClickListener {
            if(currentBook.uid == mAuth.currentUser?.uid!!)
            {
                val intent = Intent(context , myBookDetails::class.java)
                intent.putExtra("book_id" , bid)
                context.startActivity(intent)
            }
            else if(currentBook.uid != mAuth.currentUser?.uid!!)
            {
                val intent = Intent(context , BookDetails::class.java)
                intent.putExtra("book_id" , bid)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}

class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    val image = itemView.findViewById<ImageView>(R.id.bookImage)
    val title = itemView.findViewById<TextView>(R.id.title)
    val price = itemView.findViewById<TextView>(R.id.price_of_book)

}
