package com.example.krupalcollege

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.krupalcollege.Adapter.CartAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BuyActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: MutableList<Cake>
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_buy)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_shopping

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                    true
                }
                R.id.bottom_shopping -> true
                R.id.bottom_settings -> {
                    startActivity(Intent(applicationContext, SettingActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_profile -> {
                    startActivity(Intent(applicationContext, AccountActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }

        //write to the other code
        listView = findViewById(R.id.listViewCart)

        cartList = mutableListOf()
        cartAdapter = CartAdapter(this, cartList)
        listView.adapter = cartAdapter

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("cart")

        loadCartItems()

    }

    private fun loadCartItems() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartList.clear()
                    for (cartSnapshot in snapshot.children) {
                        val cartItem = cartSnapshot.getValue(Cake::class.java)
                        cartItem?.let { cartList.add(it) }
                    }
                    cartAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@BuyActivity, "Failed to load cart", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}