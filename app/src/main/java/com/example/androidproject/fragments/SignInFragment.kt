package com.example.androidproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.androidproject.*
import com.example.androidproject.activities.HomeActivity
import com.example.androidproject.models.login.Login
import com.example.androidproject.models.login.loginData
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import android.content.Context

import android.content.SharedPreferences





class SignInFragment : Fragment() {

    var root: View?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_sign_in, container, false)

        root!!.btn_signin.setOnClickListener {
            if(emailTxt.text!!.isNotEmpty() && pwdTxt.text!!.isNotEmpty())
                login(emailTxt.text.toString(), pwdTxt.text.toString())
            else
                Toast.makeText(context, "Please provide all details", Toast.LENGTH_LONG).show()
        }
        return root
    }

    fun login(email: String, password: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val login = Login(email.trim(), password.trim())
        //defining the call
        val call: Call<loginData>? = service.login(login)

        root!!.progressBar.visibility = View.VISIBLE
        call?.enqueue(object: Callback<loginData>{
            override fun onResponse(call: Call<loginData>?, response: retrofit2.Response<loginData>?) {
                root!!.progressBar.visibility = View.GONE
                if(response!!.isSuccessful) {
                    val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE) ?: return
                    with (sharedPref.edit()) {
                        putInt("user_id", response.body().data!!.get(0).id!!)
                        apply()
                    }
                    startActivity(Intent(context, HomeActivity::class.java) )
                }else if(response.message().equals("Bad request", ignoreCase = true)){
                    Toast.makeText(context,"Combination of this email and password doesn't exist.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<loginData>?, t: Throwable?) {
                root!!.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }
}