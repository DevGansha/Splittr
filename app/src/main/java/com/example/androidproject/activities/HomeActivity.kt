package com.example.androidproject.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.DialogInterface
import android.content.Intent

import com.google.android.material.dialog.MaterialAlertDialogBuilder




class HomeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Initialize the bottom navigation view
        //create bottom navigation view object
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_fragment)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
      val alert =  MaterialAlertDialogBuilder(this)
            .setTitle("Dialog")
            .setMessage("Do you want to logout")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i -> startActivity(Intent(this, MainActivity::class.java))  })
            .setNegativeButton("Cancel", /* listener = */ null)

        alert.show()
     }

}