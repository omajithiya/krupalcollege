package com.example.krupalcollege

import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.krupalcollege.Adapter.CakeAdapter
import com.google.firebase.database.*

class BirthdayActivity : AppCompatActivity() {

    private lateinit var gridViewCakes: GridView
    private lateinit var cakeList: MutableList<Cake>
    private lateinit var database: DatabaseReference
    private lateinit var cakeAdapter: CakeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)

        gridViewCakes = findViewById(R.id.recyclerView)
        cakeList = mutableListOf()
        cakeAdapter = CakeAdapter(this, cakeList)
        gridViewCakes.adapter = cakeAdapter

        database = FirebaseDatabase.getInstance().getReference("Birthday")

        fetchCakesFromFirebase()
    }

    override fun onResume() {
        super.onResume()
        database = FirebaseDatabase.getInstance().getReference("Birthday")  // ✅ Reinitialize reference
        fetchCakesFromFirebase()  // ✅ Fetch fresh data on re-entering
    }

    private fun fetchCakesFromFirebase() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isDestroyed || isFinishing) return

                cakeList.clear()

                for (cakeSnapshot in snapshot.children) {
                    val cake = cakeSnapshot.getValue(Cake::class.java)
                    cake?.let { cakeList.add(it) }
                }

                if (cakeList.isEmpty()) {
                    Toast.makeText(this@BirthdayActivity, "No cakes found!", Toast.LENGTH_SHORT).show()
                }

                Log.d("FirebaseData", "Cakes Loaded: ${cakeList.size}")  // ✅ Debugging log
                cakeAdapter.notifyDataSetChanged()
                setGridViewHeight(gridViewCakes)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BirthdayActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseError", error.message)  // ✅ Log Firebase errors
            }
        })
    }

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
