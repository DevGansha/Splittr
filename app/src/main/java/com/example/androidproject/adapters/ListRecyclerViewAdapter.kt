package com.example.androidproject.adapters

import android.annotation.SuppressLint
import android.content.Context
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

class ListRecyclerViewAdapter( val peopleList: List<data>) : RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.li_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(peopleList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return peopleList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bindItems(data: data) {
            val date=  LocalDate.parse(data.creation_date.split(" ")[0], DateTimeFormatter.ISO_DATE)
            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
            itemView.listName.text = data.list_name
            itemView.listDescription.text = "List made by ${data.username}"
            itemView.listDate.text = "Made on $formattedDate"
            itemView.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.listDetailFragment)
            }
        }
    }
}