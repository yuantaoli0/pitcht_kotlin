package com.example.test.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import kotlinx.android.synthetic.main.activity_confirm_password.*

class ConfirmEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_password)
        initView()
        initEvent()
    }

    fun initView(){

    }

    fun initEvent() {
        iv_back.setOnClickListener {
            onBackPressed()
        }

        tv_cancle.setOnClickListener {
            onBackPressed()
        }

        confirm_btn.setOnClickListener {
            //send confirm email
            var intent = Intent()
            intent.setClass(
                this.applicationContext,
                ResetActivity().javaClass
            )
            startActivity(intent);
        }
    }
}