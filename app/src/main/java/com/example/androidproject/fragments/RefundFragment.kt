package com.example.androidproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.EndPoints
import com.example.androidproject.R
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

        val specificListRequest = GetSpecificListRequest(46)
        //defining the call
        val call: Call<RefundListData>? = service.getRefunds(specificListRequest)

        call?.enqueue(object: Callback<RefundListData> {
            override fun onResponse(call: Call<RefundListData>?, response: retrofit2.Response<RefundListData>?) {
                if(response!!.isSuccessful) {
                    refundsLists = response.body().data!!.toMutableList()
                    if(refundsLists.size > 0) {
                        root!!.refunds_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        getRefundsList(refundsLists)
                       // root!!.items_rv.adapter = RefundRecyclerViewAdapter(refundsLists)
                    }
                }
            }
            override fun onFailure(call: Call<RefundListData>?, t: Throwable?) {
                Toast.makeText(context, "Error" , Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getRefundsList(refundsLists: List<data>){
        var refundsListString = ArrayList<String>()
        var refundFrom = ArrayList<String>()
        var refundTo = ArrayList<String>()

        if(refundsLists.size > 0) {
            refundFrom.add(refundsLists.get(0).username_from)
            refundTo.add(refundsLists.get(0).username_to)

            for (refundData in refundsLists) {
                if (!refundFrom.contains(refundData.username_from)) {
                    refundFrom.add(refundData.username_from)
                }
                if (!refundTo.contains(refundData.username_to)) {
                    refundTo.add(refundData.username_to)
                }
            }

            for (refundData in refundsLists) {

            }
        }
    }
}