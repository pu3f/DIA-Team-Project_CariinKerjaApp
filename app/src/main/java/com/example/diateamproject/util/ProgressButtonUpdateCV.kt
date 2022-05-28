package com.example.diateamproject.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.diateamproject.R

class ProgressButtonUpdateCV (context: Context, view: View) {
    var layout =  view.findViewById<CardView>(R.id.cvUpdate)
    var text = view.findViewById<TextView>(R.id.tvUpdate)
    var progress = view.findViewById<ProgressBar>(R.id.pbUpdate)

    @SuppressLint("ResourceAsColor")
    fun ActiveButton(){
        progress.visibility = View.VISIBLE
        text.text = "Please wait..."
    }

    fun FinishButton(){
        progress.visibility = View.GONE
        text.text = "Update"
    }
}