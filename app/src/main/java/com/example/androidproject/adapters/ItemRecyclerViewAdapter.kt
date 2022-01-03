package com.example.androidproject.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.models.item.data
import kotlinx.android.synthetic.main.li_item_list_detail.view.*

class ItemRecyclerViewAdapter(val itemsList: List<data>) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.li_item_list_detail, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(itemsList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return itemsList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data: data) {
            itemView.omschrijvingTxt.text = data.omschrijving
            itemView.usernameTxt.text = "Paid by ${data.username}"
            itemView.waarde.text = "${data.waarde}"
        }
    }
}