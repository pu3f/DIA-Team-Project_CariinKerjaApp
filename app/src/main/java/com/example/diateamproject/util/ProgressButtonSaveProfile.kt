package com.example.diateamproject.util

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.diateamproject.R

class ProgressButtonSaveProfile(context: Context, view: View) {
    var layout =  view.findViewById<CardView>(R.id.cvSaveProfile)
    var text = view.findViewById<TextView>(R.id.tvSaveProfile)
    var progress = view.findViewById<ProgressBar>(R.id.pbSaveProfile)

    fun ActiveButton(){
        progress.visibility = View.VISIBLE
        text.text = "Please wait..."
    }

    fun FinishButton(){
        progress.visibility = View.GONE
        text.text = "Save"
    }
}