package com.example.krupalcollege.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.krupalcollege.Cake
import com.example.krupalcollege.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(private val context: Context, private val cartList: MutableList<Cake>) : BaseAdapter() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("cart")

    override fun getCount(): Int = cartList.size
    override fun getItem(position: Int): Any = cartList[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)

        val ivCakeImage: ImageView = view.findViewById(R.id.ivCakeImageCart)
        val tvCakeName: TextView = view.findViewById(R.id.tvCakeNameCart)
        val tvCakePrice: TextView = view.findViewById(R.id.tvCakePriceCart)
        val btnRemove: ImageView = view.findViewById(R.id.btnRemoveCartItem)

        val cartItem = getItem(position) as Cake

        tvCakeName.text = cartItem.name
        tvCakePrice.text = "₹${cartItem.price}"
        Glide.with(context).load(cartItem.imageUrl).into(ivCakeImage)

        btnRemove.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userId)
                database.child(cartList[position].id).removeValue().addOnSuccessListener {
                    Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()
                    cartList.removeAt(position)
                    notifyDataSetChanged()  // ✅ Update UI after removing an item
                }
            }
        }


        return view

    }

}