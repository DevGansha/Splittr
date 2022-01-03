package com.example.androidproject.network

import com.example.androidproject.models.expense.AddExpenseRequest
import com.example.androidproject.models.item.ItemRequest
import com.example.androidproject.models.item.ResponseDataItem
import com.example.androidproject.models.list.CreateListRequest
import com.example.androidproject.models.list.EditListRequest
import com.example.androidproject.models.list.ListData
import com.example.androidproject.models.list.MyListRequest
import com.example.androidproject.models.login.Login
import com.example.androidproject.models.login.loginData
import com.example.androidproject.models.refund.RefundListData
import com.example.androidproject.models.signup.SignupRequest
import com.example.androidproject.models.signup.ResponseData
import com.example.androidproject.models.specificlist.GetSpecificListRequest
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @POST("LOGIN.php")
    fun login(@Body login: Login): Call<loginData>?

    @POST("SIGNUP.php")
    fun signup(@Body signupRequest: SignupRequest): Call<ResponseData>?

    @POST("LISTSget.php")
    fun getMyList(@Body myListRequest: MyListRequest): Call<ListData>?

    @POST("SHAREDLISTSget.php")
    fun getSharedList(@Body myListRequest: MyListRequest): Call<ListData>?

    @POST("LISTadd.php")
    fun createList(@Body createListRequest: CreateListRequest): Call<ResponseData>?

    @POST("LIST_EMAILSget.php")
    fun getListEmails(@Body getSpecificListRequest: GetSpecificListRequest): Call<com.example.androidproject.models.specificlist.ListData>

    @POST("ITEMadd.php")
    fun addExpense(@Body addExpenseRequest: AddExpenseRequest): Call<ResponseData>?

    @POST("ITEMSget.php")
    fun itemsGet(@Body itemRequest: ItemRequest): Call<ResponseDataItem>?

    @POST("REFUNDSget.php")
    fun getRefunds(@Body getSpecificListRequest: GetSpecificListRequest): Call<RefundListData>?

    @POST("LISTdelete.php")
    fun deleteList(@Body getSpecificListRequest: GetSpecificListRequest): Call<ResponseData>?

    @POST("LISTedit.php")
    fun editList(@Body editListRequest: EditListRequest): Call<ResponseData>?
}