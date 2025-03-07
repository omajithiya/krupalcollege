package com.example.krupalcollege

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.GridView
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krupalcollege.Adapter.CakeAdapter
import com.example.krupalcollege.Adapter.catagoryAdapter
import com.example.krupalcollege.Class.catagoryclass
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewCategoryList: RecyclerView
    private lateinit var adapter: catagoryAdapter

    private lateinit var gridViewCakes: GridView
    private lateinit var cakeList: MutableList<Cake>
    private lateinit var database: DatabaseReference
    private lateinit var cakeAdapter: CakeAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check login status
        val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            // If not logged in, redirect to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> true
                R.id.bottom_shopping -> {
                    startActivity(Intent(applicationContext, BuyActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
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

        recyclerViewCategoryList = findViewById(R.id.recyclerView)
        recyclerViewCategory()

        gridViewCakes = findViewById(R.id.recyclerView2)
        cakeList = mutableListOf()
        database = FirebaseDatabase.getInstance().getReference("cakes")

        // Retrieve data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isDestroyed || isFinishing) return // Avoid crash if activity is not active

                cakeList.clear()
                for (cakeSnapshot in snapshot.children) {
                    val cake = cakeSnapshot.getValue(Cake::class.java)
                    cake?.let {
                        cakeList.add(it)
                    }
                }

                cakeAdapter = CakeAdapter(this@MainActivity, cakeList)
                gridViewCakes.adapter = cakeAdapter

                setGridViewHeight(gridViewCakes) // Only update UI if still valid
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })

        // Initialize Firebase services
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val usernmshow = findViewById<TextView>(R.id.textView)

        // Fetch user data from Firestore
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name")
                        val email = document.getString("email")
                        val phone = document.getString("phone")

                        usernmshow.text = "Hlw $name"
//                        phoneText.text = phone ?: "No Phone"
//                        emailText.text = email ?: "No Email"
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun recyclerViewCategory() {
        val linearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategoryList.layoutManager = linearLayoutManager

        val categoryList = arrayListOf(
            catagoryclass("Birthday", "cat_1"),
            catagoryclass("Anniversary", "cat_2"),
            catagoryclass("Regular", "cat_3"),
            catagoryclass("Photo", "cat_4"),
            catagoryclass("Designer", "cat_5")
        )

        adapter = catagoryAdapter(categoryList)
        recyclerViewCategoryList.adapter = adapter
    }

    // ✅ Function to dynamically set GridView height
    private fun setGridViewHeight(gridView: GridView) {
        val listAdapter: ListAdapter = gridView.adapter ?: return

        var totalHeight = 0
        val items = listAdapter.count
        val rows = (items / gridView.numColumns) + (items % gridView.numColumns)

        for (i in 0 until rows) {
            val listItem = listAdapter.getView(i, null, gridView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        val params = gridView.layoutParams
        params.height = totalHeight + (gridView.verticalSpacing * (rows - 1))
        gridView.layoutParams = params
        gridView.requestLayout()
    }
}
