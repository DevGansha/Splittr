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
import com.example.androidproject.models.list.shareListRequest
import com.example.androidproject.models.shareList.EmailsResponse
import com.example.androidproject.models.signup.ResponseData
import com.example.androidproject.models.specificlist.GetSpecificListRequest
import com.example.androidproject.models.specificlist.ListData
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_new_list.view.*
import kotlinx.android.synthetic.main.fragment_share_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShareListFragment : Fragment() {

    var emailString = ""
    var emailToDisplay= ""
    var root: View?= null
    var list_id: Int?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_share_list, container, false)

//        getUserEmails()
        getAllEmails()

        list_id = arguments?.getInt("list_id")

        root!!.btn_plusList.setOnClickListener {
            if(root!!.shareListTxt.text.toString().trim().isNotBlank()) {
                if (doesEmailExists(root!!.shareListTxt.text.toString().trim())) {
                    emailToDisplay += root!!.shareListTxt.text.toString().trim() + "\n"
                    root!!.emailStrings.text = emailToDisplay
                    root!!.shareListTxt.setText("")
                }else{
                    root!!.shareListTxt.setFocusable(true)
                    Toast.makeText(context,"This email has not an account yet.", Toast.LENGTH_LONG).show()
                }
            }
        }
        root!!.btn_shareList.setOnClickListener {
            if(emailToDisplay.isNotBlank()){
                addEmails(emailToDisplay)
            }
        }

        return root
    }

    fun doesEmailExists(email: String): Boolean{
        val emails = emailString.split(" ")

        if(emails.contains(email))
            return true

        return false
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
                    for(email in response.body().data!!) {
                        emailString += (email.email + " ")
                    }
                }
            }
            override fun onFailure(call: Call<ListData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getAllEmails(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        //defining the call
        val call: Call<EmailsResponse>? = service.getAllEmails() // getListEmails(specificListRequest)

        call!!.enqueue(object: Callback<EmailsResponse> {
            override fun onResponse(call: Call<EmailsResponse>?, response: retrofit2.Response<EmailsResponse>?) {
                if(response!!.isSuccessful) {
                    for(email in response.body().data!!) {
                        emailString += (email.email + " ")
                    }
                }
            }
            override fun onFailure(call: Call<EmailsResponse>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    fun addEmails(emailToDisplay: String){
        val emails = emailToDisplay.split("\n")


        for(email in emails){
            shareList(email)
        }
    }

    fun shareList(emailToDisplay: String){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val shareListFragment = shareListRequest(emailToDisplay, list_id!!)

        //defining the call
        val call: Call<ResponseData>? = service.shareList(shareListFragment)

        root!!.progressbar.visibility = View.VISIBLE
        call?.enqueue(object: Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>?, response: retrofit2.Response<ResponseData>?) {
                if(response!!.isSuccessful) {
                    root!!.progressbar.visibility = View.GONE
                    Navigation.findNavController(root!!).popBackStack()
                }
            }
            override fun onFailure(call: Call<ResponseData>?, t: Throwable?) {
                root!!.progressbar.visibility = View.GONE
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

}