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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.EndPoints
import com.example.androidproject.R
import com.example.androidproject.adapters.RefundRecyclerViewAdapter
import com.example.androidproject.models.refund.RefundListData
import com.example.androidproject.models.refund.data
import com.example.androidproject.models.specificlist.GetSpecificListRequest
import com.example.androidproject.network.APIService
import kotlinx.android.synthetic.main.fragment_refund.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RefundFragment : Fragment() {

    var root: View?= null
    var refundsLists = mutableListOf<data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_refund, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRefunds()
    }

    fun getRefunds(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Defining retrofit api service
        val service = retrofit.create(APIService::class.java)

        val specificListRequest = GetSpecificListRequest(findListId())
        //defining the call
        val call: Call<RefundListData>? = service.getRefunds(specificListRequest)

        call?.enqueue(object: Callback<RefundListData> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<RefundListData>?, response: retrofit2.Response<RefundListData>?) {
                if(response!!.isSuccessful) {
                    refundsLists = response.body().data!!.toMutableList()
                    if(refundsLists.size > 0) {
                        root!!.refunds_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        root!!.refunds_list.adapter = RefundRecyclerViewAdapter(getRefundsList(refundsLists))
                    }
                }
            }
            override fun onFailure(call: Call<RefundListData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getRefundsList(refundsLists: List<data>): List<String>{

        var refundsListString = ArrayList<String>()
        if(refundsLists.size > 0) {
            val amountLog = HashMap<String, Int>()

            for(refund in refundsLists){
                val string = refund.username_to + "-" + refund.username_from
                val reverseString = refund.username_from + "-"+ refund.username_to
                if(amountLog.containsKey(string)) {
                    val amount = refund.waarde.toDouble().toInt() + amountLog.getValue(string).toInt()
                    amountLog.put(string, amount)
                }else if(amountLog.containsKey(reverseString)){
                    val amount = refund.waarde.toDouble().toInt() - amountLog.getValue(reverseString).toInt()
                    amountLog.put(reverseString, amount * (-1))
                }
                else{
                    amountLog.put(string, refund.waarde.toDouble().toInt())
                }
            }

            amountLog.forEach { s, i ->
                var str = "User " + s.substringBefore("-") + " owes user " + s.substringAfter("-") + " : " + i.toString()
                refundsListString.add(str)
            }
        }

        return refundsListString.toMutableList()
    }

    fun findListId(): Int{
        val sharedPref = activity?.getSharedPreferences("splitter", Context.MODE_PRIVATE)
        val list_id = sharedPref!!.getInt("list_id", 0)

        return list_id
    }
}