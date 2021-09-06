package com.example.test.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.feng.kotlin.AppContext
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        initView()
        initEvent()
    }

    fun initView(){
        et_email.setText(AppContext.user?.email);
    }

    fun initEvent() {
        confirm_btn.setOnClickListener {
            if(et_email.text.trim().isEmpty()){
                Toast.makeText(this, "Email not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            var intent = Intent()
            intent.setClass(
                this.applicationContext,
                ConfirmEditActivity().javaClass
            )
            startActivity(intent);
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        tv_cancle.setOnClickListener {
            onBackPressed()
        }
    }
}