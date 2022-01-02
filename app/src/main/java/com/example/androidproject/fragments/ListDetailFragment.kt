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
import com.example.androidproject.models.specificlist.GetSpecificListRequest
import com.example.androidproject.models.specificlist.ListData
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_list_detail.view.*
import kotlinx.android.synthetic.main.fragment_new_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListDetailFragment : Fragment() {

    var root: View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_list_detail, container, false)

        root!!.addExpenseBtn.setOnClickListener {
            Navigation.findNavController(root!!).navigate(R.id.addExpenseFragment)
        }

        root!!.showRefundsBtn.setOnClickListener {

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserEmails()
    }

    fun getUserEmails(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getInt("user_id", 0)

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val specificListRequest = GetSpecificListRequest(46)
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
}