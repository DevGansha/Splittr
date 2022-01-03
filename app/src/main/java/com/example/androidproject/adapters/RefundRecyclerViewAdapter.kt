package com.example.androidproject.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.models.list.data
import kotlinx.android.synthetic.main.li_item.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RefundRecyclerViewAdapter( val refundList: List<String>) : RecyclerView.Adapter<RefundRecyclerViewAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefundRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.li_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RefundRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(refundList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return refundList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data: String) {
            itemView.listName.text = data
        }
    }
}