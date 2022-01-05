package com.example.androidproject.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.androidproject.EndPoints
import com.example.androidproject.R
import com.example.androidproject.models.list.ListData
import com.example.androidproject.models.list.MyListRequest
import com.example.androidproject.models.list.data
import com.example.androidproject.models.signup.ResponseData
import com.example.androidproject.models.specificlist.GetSpecificListRequest
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_delete.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeleteFragment : Fragment() {

    var root: View ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_delete, container, false)
        root!!.deletelist.setOnClickListener {
            deleteList()
        }
        root!!.cancel_btn.setOnClickListener {
            Navigation.findNavController(root!!).navigate(R.id.myListsFragment)
        }
        return root
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

        root!!.progressBar.visibility = View.VISIBLE

        call!!.enqueue(object: Callback<ResponseData> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ResponseData>?, response: retrofit2.Response<ResponseData>?) {
                if(response!!.isSuccessful) {
                    root!!.progressBar.visibility = View.GONE
                    Navigation.findNavController(root!!).navigate(R.id.myListsFragment)
                }
            }
            override fun onFailure(call: Call<ResponseData>?, t: Throwable?) {
                root!!.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }
    fun findListId(): Int{
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE)
        val list_id = sharedPref!!.getInt("list_id", 0)

        return list_id
    }
}