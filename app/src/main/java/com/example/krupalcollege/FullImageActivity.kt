
package com.example.krupalcollege

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FullImageActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_full_image)

        val fullImageView = findViewById<ImageView>(R.id.fullImageView)
        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewPrice = findViewById<TextView>(R.id.textViewPrice)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)
        val addtoCartBtn = findViewById<Button>(R.id.BuybtnCkiFull)

        val imageUrl = intent.getStringExtra("imageUrl")
        val cakeName = intent.getStringExtra("cakeName")
        val cakePrice = intent.getStringExtra("cakePrice")
        val cakeDescription = intent.getStringExtra("description")

        // ✅ Get Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("cart")

        // Load image
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.loding_image)
            .error(R.drawable.noimage)
            .into(fullImageView)

        // Set text
        textViewName.text = cakeName
        textViewPrice.text = "Price: ₹$cakePrice"
        textViewDescription.text = "Description: $cakeDescription" ?: "No description available"

        addtoCartBtn.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val cartItem = mapOf(
                    "imageUrl" to imageUrl,
                    "cakeName" to cakeName,
                    "cakePrice" to cakePrice,
                    "cakeDescription" to cakeDescription
                )

                // Save cart item in Firebase under user's ID
                database.child(userId).push().setValue(cartItem).addOnSuccessListener {
                    Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext,BuyActivity::class.java))
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to add to Cart", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            }
        }

    }

}