package com.example.androidproject.models.expense

data class AddExpenseRequest(var user_email: String, var list_id: Int, var desc: String, var price: Int)
