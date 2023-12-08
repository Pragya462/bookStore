package com.example.bookstore

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(){

    private lateinit var mAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookList: ArrayList<books>
    private lateinit var adapter: BookAdapter
    private lateinit var dbRef : DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var header: View

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loading = loadingClass(this)
        loading.startLoading()

        val handler = Handler()
        handler.postDelayed(object: Runnable
        {
            override fun run(){
                loading.isDismiss()
            }
        }, 3000)

        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()
        storage = FirebaseStorage.getInstance()
        bookList = ArrayList()
        adapter = BookAdapter(this,bookList)

        val drawer_layout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        header = navView.getHeaderView(0)

        var userImage = header.findViewById<ImageView>(R.id.profile_image)
        var username = header.findViewById<TextView>(R.id.username)

        dbRef.child("Users").child(mAuth.currentUser?.uid!!).get().addOnSuccessListener {
            if(it.exists())
            {
                val pImage = it.child("profile_pic").value
                Picasso.get().load(pImage.toString()).placeholder(R.drawable.userpic).into(userImage)
                val pName = it.child("name").value.toString()

                username.text = pName
            }
            else{
                Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Some error occurred. Please try again after some time", Toast.LENGTH_LONG).show()
        }

        userImage.setOnClickListener{
            intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.my_ads -> {
                    intent = Intent(this, MyAds::class.java)
                    startActivity(intent)
                }
                R.id.sell_book -> {
                    intent = Intent(this, SellBook::class.java)
                    startActivity(intent)
                }
                R.id.logout -> {
                    intent = Intent(this, Login::class.java)
                    finish()
                    startActivity(intent)
                }
            }
            true
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =adapter

        dbRef.child("Books").addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookList.clear()
                for(postSnapshot in snapshot.children)
                {
                    val currentBook = postSnapshot.getValue(books::class.java)
                    if(currentBook?.status == "unsold")
                    {
                        bookList.add(currentBook!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

}