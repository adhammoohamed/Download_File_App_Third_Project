package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = intent.getStringExtra("fileName")
        val status = intent.getStringExtra("status")

        val fileNameTv = findViewById<TextView>(R.id.file_name_tv)
        fileNameTv.text = fileName

        val statusTv = findViewById<TextView>(R.id.status_tv)
        //check the status
        if (status == "Success"){
            statusTv.text = status
        }else{
            statusTv.text = status
            statusTv.setTextColor(Color.RED)
        }

        val okBtn = findViewById<Button>(R.id.ok_btn)
        okBtn.setOnClickListener {
            //navigate to MainActivity
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
        }
    }

}
