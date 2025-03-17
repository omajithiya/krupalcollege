package com.example.krupalcollege

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
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

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var cartListView: ListView
    private lateinit var emptyTextView: TextView
    private lateinit var cartAdapter: CartAdapter
    private val cartList = mutableListOf<Cake>()
    private val cartKeys = mutableListOf<String>()
    private lateinit var totapriseview:TextView

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
        cartListView = findViewById(R.id.cartListView)
        emptyTextView = findViewById(R.id.emptyTextView) // Add this TextView in your XML layout
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("cart")

        val userId = auth.currentUser?.uid

//        if (userId != null) {
//            database.child(userId).addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    cartList.clear()
//                    cartKeys.clear()
//
//                    for (itemSnapshot in snapshot.children) {
//                        val cake = itemSnapshot.getValue(Cake::class.java)
//                        val key = itemSnapshot.key
//
//                        if (cake != null && key != null) {
//                            cartList.add(cake)
//                            cartKeys.add(key)
//                        }
//                    }
//
//                    cartAdapter.notifyDataSetChanged() // Refresh the adapter
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(this@BuyActivity, "Failed to load cart", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
        if (userId != null) {
            database.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartList.clear()
                    cartKeys.clear()
                    var totalPrice = 0.0  // Variable to store total price

                    for (itemSnapshot in snapshot.children) {
                        val cake = itemSnapshot.getValue(Cake::class.java)
                        val key = itemSnapshot.key
                        if (cake != null && key != null) {
                            cartList.add(cake)
                            cartKeys.add(key)

                            // Assuming each cake object has a `price` field
                            val cakePrice = cake.price.toDoubleOrNull() ?: 0.0
                            totalPrice += cakePrice
                        }
                    }
                    if (cartList.isEmpty()) {
                        cartListView.visibility = View.GONE
                        emptyTextView.visibility = View.VISIBLE // Show "Cart is empty" message
                    } else {
                        cartListView.visibility = View.VISIBLE
                        emptyTextView.visibility = View.GONE // Hide message
                        totapriseview.text = "Total: ₹$totalPrice"
                        cartAdapter = CartAdapter(this@BuyActivity, cartList, cartKeys)
                        cartListView.adapter = cartAdapter
                    }
//                    cartAdapter = CartAdapter(this@BuyActivity, cartList, cartKeys)
//                    cartListView.adapter = cartAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@BuyActivity, "Failed to load cart", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }

        totapriseview = findViewById<TextView>(R.id.totalPriceTextView)
        val checkoutbtn = findViewById<Button>(R.id.checkoutButton)

        checkoutbtn.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
//                val intent = Intent(this, PaymentActivity::class.java)
//                intent.putExtra("TOTAL_PRICE", totalPrice)  // Pass total price to next screen
                startActivity(intent)
            }
        }

    }

}