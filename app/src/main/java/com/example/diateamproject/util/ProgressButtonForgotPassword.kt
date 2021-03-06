package com.example.diateamproject.util

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.diateamproject.R

class ProgressButtonForgotPassword(context: Context, view: View) {
    var layout =  view.findViewById<CardView>(R.id.cvForgotPass)
    var text = view.findViewById<TextView>(R.id.tvForgotPass)
    var progress = view.findViewById<ProgressBar>(R.id.pbForgotPass)

    fun ActiveButton(){
        progress.visibility = View.VISIBLE
        text.text = "Please wait..."
    }

    fun FinishButton(){
        progress.visibility = View.GONE
        text.text = "Send Email"
    }
}