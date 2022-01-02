package com.example.androidproject.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidproject.R

class MainActivity : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "splitter"
    var sharedPref: SharedPreferences?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    }
}