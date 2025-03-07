package com.example.krupalcollege.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.krupalcollege.Class.catagoryclass
import com.example.krupalcollege.R
import com.bumptech.glide.Glide
import com.example.krupalcollege.AnnivarsaryActivity
import com.example.krupalcollege.BirthdayActivity
import com.example.krupalcollege.DesigenActivity
import com.example.krupalcollege.PhotoActivity
import com.example.krupalcollege.RegularActivity

class catagoryAdapter(private val catagoryclass: ArrayList<catagoryclass>) :
    RecyclerView.Adapter<catagoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = catagoryclass[position]
        holder.categoryName.text = category.title

        val picUrl = when (position) {
            0 -> {
                holder.mainLayout.setBackgroundResource(R.drawable.cat_backgroud1)
                setClickListener(holder.itemView.context, holder, BirthdayActivity::class.java)
//                Toast.makeText(this, "Birthday cake success", Toast.LENGTH_SHORT).show()
                "display_cake"
            }

            1 -> {
                holder.mainLayout.setBackgroundResource(R.drawable.cat_backgroud2)
                setClickListener(holder.itemView.context, holder, AnnivarsaryActivity::class.java)
                "display_cake2"
            }

            2 -> {
                holder.mainLayout.setBackgroundResource(R.drawable.cat_backgroud3)
                setClickListener(holder.itemView.context, holder, RegularActivity::class.java)
                "display_cake3"
            }

            3 -> {
                holder.mainLayout.setBackgroundResource(R.drawable.cat_backgroud4)
                setClickListener(holder.itemView.context, holder, PhotoActivity::class.java)
                "selfi_photo"
            }

            4 -> {
                holder.mainLayout.setBackgroundResource(R.drawable.cat_backgroud5)
                setClickListener(holder.itemView.context, holder, DesigenActivity::class.java)
                "display_cake4"
            }

            else -> ""
        }

        val drawableResourceId = holder.itemView.context.resources.getIdentifier(
            picUrl, "drawable", holder.itemView.context.packageName
        )

        Glide.with(holder.itemView.context)
            .load(drawableResourceId)
            .into(holder.categoryPic)

    }

    override fun getItemCount(): Int {
        return catagoryclass.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryPic: ImageView = itemView.findViewById(R.id.cataegoryPic)
        val mainLayout: ConstraintLayout = itemView.findViewById(R.id.mainLayout)
    }
    private fun setClickListener(context: Context, holder: ViewHolder, activityClass: Class<*>) {
        holder.mainLayout.setOnClickListener {
            val intent = Intent(context, activityClass)
            context.startActivity(intent)
        }
    }
}