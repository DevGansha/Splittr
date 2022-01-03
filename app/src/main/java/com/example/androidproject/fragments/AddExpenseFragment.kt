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
import com.example.androidproject.models.expense.AddExpenseRequest
import com.example.androidproject.models.signup.ResponseData
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_add_expense.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddExpenseFragment : Fragment() {

    var root: View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_expense, container, false)

        root!!.addItemInList.setOnClickListener {
            if(root!!.emailTxt.text.toString().isNotEmpty() && root!!.paidForWhoTxt.text.toString().isNotEmpty() &&
                root!!.descTxt.text.toString().isNotEmpty() && root!!.priceTxt.text.toString().isNotEmpty()) {
                if (isEmailExists(root!!.emailTxt.text.toString().trim(), root!!.paidForWhoTxt.text.toString().trim()))
                    addItemInList(root!!.emailTxt.text.toString().trim(), findListId(), root!!.descTxt.text.toString().trim(), Integer.valueOf(root!!.priceTxt.text.toString().trim()))
            }else{
                Toast.makeText(context, "Must input all fields", Toast.LENGTH_LONG).show()
            }
        }

        return root
    }

    fun findListId(): Int{
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE)
        val list_id = sharedPref!!.getInt("list_id", 0)

        return list_id
    }

    fun isEmailExists(first_email: String, second_email: String): Boolean{
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE)
        val list_emails = sharedPref?.getString("list_emails", "")

        val emails = list_emails!!.split(" ")

        if(emails.contains(first_email) && emails.contains(second_email))
            return true
        else if(!emails.contains(first_email))
            Toast.makeText(context, "The first email is not part of this list.", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, "The second email is not part of this list.", Toast.LENGTH_LONG).show()


        return false
    }
    fun addItemInList(first_email: String, list_id: Int, desc: String, price: Int){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
        val user_id = sharedPref.getInt("user_id", 0)

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val addExpenseFragment = AddExpenseRequest(first_email, list_id, desc, price)
        //defining the call
        val call: Call<ResponseData>? = service.addExpense(addExpenseFragment)

        call?.enqueue(object: Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>?, response: retrofit2.Response<ResponseData>?) {
                if(response!!.isSuccessful && response.body().code!!.equals(7)) {
                    Navigation.findNavController(root!!).popBackStack()
                }
            }
            override fun onFailure(call: Call<ResponseData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }


}