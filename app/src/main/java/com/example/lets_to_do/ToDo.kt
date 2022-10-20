package com.example.lets_to_do

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lets_to_do.Db.MyAdapter
import com.example.lets_to_do.Db.MyDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDo : AppCompatActivity() {

    val myAdapter = MyAdapter(ArrayList(), this)
    val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)
        init()

        val ic_add = findViewById<FloatingActionButton>(R.id.icAdd)

        ic_add.setOnClickListener{
            val edit_activity = Intent(this, EditActivity::class.java)
            startActivity(edit_activity)
        }

    }

    fun init(){
        val rcView = findViewById<RecyclerView>(R.id.rcView)
        rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView.adapter = myAdapter
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun fillAdapter(){
        val textView = findViewById<TextView>(R.id.textView)
        val list = myDbManager.readDbData()
        myAdapter.updateAdapter(list)
        if (list.size > 0) textView.visibility = View.GONE
        else textView.visibility = View.VISIBLE
    }

    private fun getSwapMg():ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }
        })
    }

}