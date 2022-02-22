package com.example.diateamproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.diateamproject.R

class JobDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)
        var jobDetailId = intent.getIntExtra("jobDetailId", 0)
        Log.d("Success", jobDetailId.toString())
    }
}