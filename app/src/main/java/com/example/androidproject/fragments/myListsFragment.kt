package com.example.androidproject.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.EndPoints
import com.example.androidproject.R
import com.example.androidproject.adapters.ListRecyclerViewAdapter
import com.example.androidproject.adapters.MyListsItemRecyclerAdapter
import com.example.androidproject.adapters.OnItemClickListener
import com.example.androidproject.models.list.ListData
import com.example.androidproject.models.list.MyListRequest
import com.example.androidproject.models.list.data
import com.example.androidproject.models.signup.ResponseData
import com.example.androidproject.models.specificlist.GetSpecificListRequest
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_my_lists.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class myListsFragment : Fragment(), OnItemClickListener {

    var root: View?=null
    var myLists = mutableListOf<data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_my_lists, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyLists()
    }


    fun getMyLists(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getInt("user_id", 0)

        val myListRequest = MyListRequest(user_id)
        //defining the call
        val call: Call<ListData>? = service.getMyList(myListRequest)

        call?.enqueue(object: Callback<ListData> {
            override fun onResponse(call: Call<ListData>?, response: retrofit2.Response<ListData>?) {
                if(response!!.isSuccessful) {
                    myLists.addAll(response.body().data!!) //.toMutableList()
                    if(myLists.size > 0) {
                        root!!.myList_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        root!!.myList_rv.adapter = MyListsItemRecyclerAdapter(myLists, this@myListsFragment)
                    }else{
                        root!!.myList_rv.visibility = View.GONE
                        root!!.myList_empty.visibility = View.VISIBLE
                    }
                }
            }
            override fun onFailure(call: Call<ListData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClicked(btnClick:String, listData: data) {
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("list_id", listData.id)
            apply()
        }
        if(btnClick.equals("DELETE", true)){
            deleteList()
        }
    }

    fun findListId(): Int{
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE)
        val list_id = sharedPref!!.getInt("list_id", 0)

        return list_id
    }

    fun deleteList(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val specificListRequest = GetSpecificListRequest(findListId())
        //defining the call
        val call: Call<ResponseData>? = service.deleteList(specificListRequest)

        call!!.enqueue(object: Callback<ResponseData> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ResponseData>?, response: retrofit2.Response<ResponseData>?) {
                if(response!!.isSuccessful) {
                    myLists.clear()
                    updateMyLists()
                }
            }
            override fun onFailure(call: Call<ResponseData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updateMyLists(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getInt("user_id", 0)

        val myListRequest = MyListRequest(user_id)
        //defining the call
        val call: Call<ListData>? = service.getMyList(myListRequest)

        call?.enqueue(object: Callback<ListData> {
            override fun onResponse(call: Call<ListData>?, response: retrofit2.Response<ListData>?) {
                if(response!!.isSuccessful) {
                    myLists.addAll(response.body().data!!) //.toMutableList()
                    root!!.myList_rv.adapter!!.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<ListData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }
}