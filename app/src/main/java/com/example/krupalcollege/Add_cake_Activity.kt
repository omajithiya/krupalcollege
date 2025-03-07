package com.example.krupalcollege

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Add_cake_Activity : AppCompatActivity() {

    private lateinit var etCakeName: EditText
    private lateinit var etCakeDescription: EditText
    private lateinit var etCakePrice: EditText
    private lateinit var etCakeImageUrl: EditText
    private lateinit var ivCakePreview: ImageView
    private lateinit var btnAddCake: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var spinner: Spinner
    private lateinit var adapterItems: ArrayAdapter<String>

    var items = arrayOf(
        "Main bord cake",
        "Birthday cake upload",
        "Anniversary cake upload",
        "Regular cake upload",
        "Photo cake upload",
        "Designer cake upload"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.krupalcollege.R.layout.activity_add_cake)

        etCakeName = findViewById(R.id.etCakeName)
        etCakeDescription = findViewById(R.id.etCakeDescription)
        etCakePrice = findViewById(R.id.etCakePrice)
        etCakeImageUrl = findViewById(R.id.etCakeImageUrl)
        ivCakePreview = findViewById(R.id.ivCakePreview)
        btnAddCake = findViewById(R.id.btnAddCake)
        spinner = findViewById(R.id.spinner)

        // Default database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("cakes")

        // Load image preview when user types the URL
        etCakeImageUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val imageUrl = s.toString()
                if (imageUrl.isNotEmpty()) {
                    ivCakePreview.visibility = View.VISIBLE
                    Glide.with(this@Add_cake_Activity).load(imageUrl).into(ivCakePreview)
                } else {
                    ivCakePreview.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Spinner setup
        adapterItems = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapterItems

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedOption = items[position]
                databaseReference = when (selectedOption) {
                    "Main bord cake"-> FirebaseDatabase.getInstance().getReference("cakes")
                    "Birthday cake upload" -> FirebaseDatabase.getInstance().getReference("Birthday")
                    "Anniversary cake upload" -> FirebaseDatabase.getInstance().getReference("Anniversary")
                    "Regular cake upload" -> FirebaseDatabase.getInstance().getReference("Regular")
                    "Photo cake upload" -> FirebaseDatabase.getInstance().getReference("Photo")
                    "Designer cake upload" -> FirebaseDatabase.getInstance().getReference("Design")
                    else -> FirebaseDatabase.getInstance().getReference("cakes")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Button Click to Add Cake
        btnAddCake.setOnClickListener {
            addCakeToFirebase()
        }
    }

    private fun addCakeToFirebase() {
        val name = etCakeName.text.toString().trim()
        val description = etCakeDescription.text.toString().trim()
        val price = etCakePrice.text.toString().trim()
        val imageUrl = etCakeImageUrl.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val cakeId = databaseReference.push().key!!  // Generate unique ID
        val newCake = Cake(name, description, imageUrl, price)

        databaseReference.child(cakeId).setValue(newCake)
            .addOnSuccessListener {
                Toast.makeText(this, "Cake Added!", Toast.LENGTH_SHORT).show()
                finish()  // Close activity after adding
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add cake!", Toast.LENGTH_SHORT).show()
            }
    }
}
