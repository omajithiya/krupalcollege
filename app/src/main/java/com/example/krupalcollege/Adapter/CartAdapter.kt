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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(private val context: Context, private var cartList: MutableList<Cake>, private val cartKeys: MutableList<String>) : BaseAdapter() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("cart")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun getCount(): Int = cartList.size

    override fun getItem(position: Int): Any = cartList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)

        val ivCakeImage: ImageView = view.findViewById(R.id.ivCartCakeImage)
        val tvCakeName: TextView = view.findViewById(R.id.tvCartCakeName)
        val tvCakePrice: TextView = view.findViewById(R.id.tvCartCakePrice)
        val ivRemoveCartItem: ImageView = view.findViewById(R.id.ivRemoveCartItem)

        val cake = getItem(position) as Cake

        tvCakeName.text = cake.name
        tvCakePrice.text = "₹${cake.price}"

        Glide.with(context)
            .load(cake.imageUrl)
            .placeholder(R.drawable.loding_image)
            .error(R.drawable.noimage)
            .into(ivCakeImage)

        // Remove Item from Cart
        ivRemoveCartItem.setOnClickListener {
            if (userId != null) {
                database.child(userId).child(cartKeys[position]).removeValue().addOnSuccessListener {
                    Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
                    cartList.removeAt(position)
                    cartKeys.removeAt(position)
                    notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
