package com.example.diateamproject.util

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.diateamproject.R

class ProgressButtonSignup(context: Context, view: View) {
    var layout =  view.findViewById<CardView>(R.id.cvSignup)
    var text = view.findViewById<TextView>(R.id.tvSignup)
    var progress = view.findViewById<ProgressBar>(R.id.pbSignup)

    fun ActiveButton(){
        progress.visibility = View.VISIBLE
        text.text = "Please wait..."
    }

    fun FailedButton(){
        progress.visibility = View.GONE
        text.text = "Sign Up"
    }

    fun SuccessButton(){
        progress.visibility = View.GONE
        text.text = "Sign Up Succeed"
    }
}