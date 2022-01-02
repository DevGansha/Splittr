package com.example.androidproject.fragments

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
import com.example.androidproject.models.list.CreateListRequest
import com.example.androidproject.models.signup.ResponseData
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_new_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewListFragment : Fragment() {
    var root: View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_list, container, false)
        root!!.btn_addList.setOnClickListener {
            createNewList()
        }
        return root
    }


    fun createNewList(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getInt("user_id", 0)

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val createListRequest = CreateListRequest(root!!.listNameTxt.text.toString(), user_id, root!!.listDescTxt.text.toString())
        //defining the call
        val call: Call<ResponseData>? = service.createList(createListRequest)

        call?.enqueue(object: Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>?, response: retrofit2.Response<ResponseData>?) {
                if(response!!.isSuccessful) {
                    Navigation.findNavController(root!!).popBackStack()
                }
            }
            override fun onFailure(call: Call<ResponseData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }
}