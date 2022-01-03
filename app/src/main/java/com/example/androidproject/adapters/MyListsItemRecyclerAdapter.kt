package com.example.androidproject.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.models.list.data
import kotlinx.android.synthetic.main.mylists_item.view.*

class MyListsItemRecyclerAdapter( val dataList: List<data>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MyListsItemRecyclerAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListsItemRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.mylists_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyListsItemRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(dataList[position], itemClickListener)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return dataList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindItems(data: data,clickListener: OnItemClickListener) {
            itemView.listName.text = data.list_name
            itemView.listDescription.text = data.omschrijving
            itemView.listDate.text = "Made on ${data.creation_date}"
            itemView.deletelist.setOnClickListener {
                clickListener.onItemClicked("DELETE", data)
            }
            itemView.editlist.setOnClickListener {
                clickListener.onItemClicked("EDIT", data)
            }
            itemView.sharelist.setOnClickListener {
                clickListener.onItemClicked("SHARE", data)
            }
        }
    }

}
interface OnItemClickListener{
    fun onItemClicked(btnClick: String, user: data)
}