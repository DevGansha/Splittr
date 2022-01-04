package com.example.androidproject.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.EndPoints
import com.example.androidproject.R
import com.example.androidproject.adapters.ItemRecyclerViewAdapter
import com.example.androidproject.models.item.ItemRequest
import com.example.androidproject.models.item.ResponseDataItem
import com.example.androidproject.models.item.data
import com.example.androidproject.models.specificlist.GetSpecificListRequest
import com.example.androidproject.models.specificlist.ListData
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_list_detail.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListDetailFragment : Fragment() {

    var root: View?=null
    var myLists = mutableListOf<data>()
    var list_obj: com.example.androidproject.models.list.data?= null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_list_detail, container, false)

        list_obj = arguments?.getParcelable<com.example.androidproject.models.list.data>("list_obj")

        val date=  LocalDate.parse(list_obj!!.creation_date.split(" ")[0], DateTimeFormatter.ISO_DATE)
        val formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))

        root!!.listName.text = list_obj!!.list_name
        root!!.listDescription.text = list_obj!!.omschrijving
        root!!.listDate.text = "Made on $formattedDate"
        root!!.addExpenseBtn.setOnClickListener {
            Navigation.findNavController(root!!).navigate(R.id.addExpenseFragment)
        }

        root!!.showRefundsBtn.setOnClickListener {
            Navigation.findNavController(root!!).navigate(R.id.refundFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserEmails()
        getItems()

    }
    fun findListId(): Int{
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE)
        val list_id = sharedPref!!.getInt("list_id", 0)

        return list_id
    }

    fun getUserEmails(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val specificListRequest = GetSpecificListRequest(findListId())
        //defining the call
        val call: Call<ListData> = service.getListEmails(specificListRequest)

        call.enqueue(object: Callback<ListData> {
            override fun onResponse(call: Call<ListData>?, response: retrofit2.Response<ListData>?) {
                if(response!!.isSuccessful) {
                    var emailString = ""
                    for(email in response.body().data!!) emailString += (email.email + " ")

                    val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
                    with (sharedPref.edit()) {
                        putString("list_emails", emailString)
                        apply()
                    }
                }
            }
            override fun onFailure(call: Call<ListData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getItems(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val itemRequest = ItemRequest(findListId())
        //defining the call
        val call: Call<ResponseDataItem>? = service.itemsGet(itemRequest)

        call?.enqueue(object: Callback<ResponseDataItem> {
            override fun onResponse(call: Call<ResponseDataItem>?, response: retrofit2.Response<ResponseDataItem>?) {
                if(response!!.isSuccessful) {
                    myLists.addAll(response.body().data!!) //.toMutableList()
                    if(myLists.size > 0) {
                        root!!.items_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        root!!.items_rv.adapter = ItemRecyclerViewAdapter(myLists)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseDataItem>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }


}