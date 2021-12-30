package com.example.androidproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.androidproject.R
import kotlinx.android.synthetic.main.fragment_main_screen.view.*

class MainScreen : Fragment() {


    var root : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_main_screen, container, false)

        root.signinBtn.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.signInFragment)
        }
        return root
    }
}