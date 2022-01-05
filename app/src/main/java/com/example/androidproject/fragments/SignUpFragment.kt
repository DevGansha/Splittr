package com.example.androidproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.androidproject.EndPoints
import com.example.androidproject.R
import com.example.androidproject.models.signup.SignupRequest
import com.example.androidproject.models.signup.ResponseData
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_sign_in.pwdTxt
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpFragment : Fragment() {

    var root: View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_sign_up, container, false)

        root!!.btn_signup.setOnClickListener {
            if(root!!.usernameTxt.text.toString().isNotBlank() && root!!.emailTxt.text.toString().isNotBlank() &&
            pwdTxt.text.toString().isNotBlank() && repeatPwdTxt.text.toString().isNotBlank()) {
                if (root!!.pwdTxt.text.toString().trim()
                        .equals(root!!.repeatPwdTxt.text.toString().trim(), true)
                ) {
                    signup(
                        root!!.usernameTxt.text.toString().trim(),
                        root!!.emailTxt.text.toString().trim(),
                        pwdTxt.text.toString().trim()
                    )
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context, "Please provide all details", Toast.LENGTH_LONG).show()
            }
        }
        return root
    }

    fun signup(username: String, email: String, password: String) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val signup = SignupRequest(username, email, password)
        //defining the call
        val call: Call<ResponseData>? = service.signup(signup)

        root!!.progressBar.visibility = View.VISIBLE
        call?.enqueue(object: Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>?, response: retrofit2.Response<ResponseData>?) {
                root!!.progressBar.visibility = View.GONE
                if(response!!.isSuccessful){
                    Navigation.findNavController(root!!).navigate(R.id.signInFragment)
                }
            }
            override fun onFailure(call: Call<ResponseData>?, t: Throwable?) {
                root!!.progressBar.visibility = View.GONE
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }
}