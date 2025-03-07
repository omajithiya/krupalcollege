package com.example.krupalcollege.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.krupalcollege.Cake
import com.example.krupalcollege.FullImageActivity
import com.example.krupalcollege.R

class CakeAdapter(private val context : Context, private val cakeList: List<Cake>) : BaseAdapter() {

    override fun getCount(): Int = cakeList.size

    override fun getItem(position: Int): Any = cakeList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_cake, parent, false)

        val ivCakeImage: ImageView = view.findViewById(R.id.ivCakeImage)
        val tvCakeName: TextView = view.findViewById(R.id.tvCakeName)
        val tvCakePrice: TextView = view.findViewById(R.id.tvCakePrice)
        val tvCakeDescription: TextView = view.findViewById(R.id.tvCakeDescription)
        val cake = getItem(position) as Cake

        // Check if the activity is still valid before using Glide
        if (context is Activity && (context.isDestroyed || context.isFinishing)) {
            return view
        }

        tvCakeName.text = cake.name
        tvCakePrice.text = "₹${cake.price}"
        tvCakeDescription.text = cake.description

        Glide.with(context)
            .load(cake.imageUrl)
            .placeholder(R.drawable.loding_image)
            .error(R.drawable.noimage)
            .into(ivCakeImage)

        // ✅ Handle Click to Open Full Image Activity
        view.setOnClickListener {
            val intent = Intent(context, FullImageActivity::class.java )
            intent.putExtra("imageUrl", cake.imageUrl)
            intent.putExtra("cakeName", cake.name)
            intent.putExtra("cakePrice", cake.price.toString())
            intent.putExtra("description",cake.description)
            context.startActivity(intent)
        }


        return view
    }


}