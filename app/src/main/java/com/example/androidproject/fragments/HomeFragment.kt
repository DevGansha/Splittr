package com.example.androidproject.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.EndPoints
import com.example.androidproject.R
import com.example.androidproject.adapters.ListRecyclerViewAdapter
import com.example.androidproject.adapters.OnItemClickedListener
import com.example.androidproject.models.list.ListData
import com.example.androidproject.models.list.MyListRequest
import com.example.androidproject.models.list.data
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.myList_empty
import kotlinx.android.synthetic.main.fragment_home.view.myList_rv
import kotlinx.android.synthetic.main.fragment_home.view.progressBar
import kotlinx.android.synthetic.main.fragment_my_lists.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(), OnItemClickedListener {

    var root: View?=null
    var myLists = mutableListOf<data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false)
        root!!.add_list_btn.setOnClickListener {
            Navigation.findNavController(root!!).navigate(R.id.newListFragment)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMyLists()
        getSharedLists()
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

        root!!.progressBar.visibility = View.VISIBLE
        call?.enqueue(object: Callback<ListData> {
            override fun onResponse(call: Call<ListData>?, response: retrofit2.Response<ListData>?) {
                if(response!!.isSuccessful) {
                    root!!.progressBar.visibility = View.GONE
                    if(response.body().data!!.size > 2) {
                        myLists = response.body().data!!.subList(0, 3).toMutableList()
                    }else{
                        myLists = response.body().data!!.toMutableList()
                    }
                    if(myLists.size > 0) {
                        root!!.myList_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        root!!.myList_rv.adapter = ListRecyclerViewAdapter(myLists, this@HomeFragment)
                    }else{
                        root!!.myList_rv.visibility = View.GONE
                        root!!.myList_empty.visibility = View.VISIBLE
                    }
                }
            }
            override fun onFailure(call: Call<ListData>?, t: Throwable?) {
                root!!.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getSharedLists(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getInt("user_id", 0)

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val myListRequest = MyListRequest(user_id)
        //defining the call
        val call: Call<ListData>? = service.getSharedList(myListRequest)

        root!!.progressBar.visibility = View.VISIBLE
        call?.enqueue(object: Callback<ListData> {
            override fun onResponse(call: Call<ListData>?, response: retrofit2.Response<ListData>?) {
                if(response!!.isSuccessful) {
                    root!!.progressBar.visibility = View.GONE
                    if(response.body().data!!.size > 2) {
                        myLists = response.body().data!!.subList(0, 3).toMutableList()
                    }else{
                        myLists = response.body().data!!.toMutableList()
                    }
                    if(myLists.size > 0) {
                        root!!.sharedList_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        root!!.sharedList_rv.adapter = ListRecyclerViewAdapter(myLists, this@HomeFragment)
                    }else{
                        root!!.sharedList_rv.visibility = View.GONE
                        root!!.sharedList_empty.visibility = View.VISIBLE
                    }
                }
            }
            override fun onFailure(call: Call<ListData>?, t: Throwable?) {
                root!!.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClicked(data: data) {
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt("list_id", data.id)
            apply()
        }
        Navigation.findNavController(root!!).navigate(HomeFragmentDirections.actionHomeFragmentToListDetailFragment(data))
    }
}