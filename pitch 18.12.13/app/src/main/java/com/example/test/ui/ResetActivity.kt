package com.example.test.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.ui.fragment.LoginFragment
import kotlinx.android.synthetic.main.activity_confirm_password.*
import kotlinx.android.synthetic.main.activity_confirm_password.iv_back
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        initView()
        initEvent()
    }

    fun initView(){

    }

    fun initEvent() {
        iv_back.setOnClickListener {
            onBackPressed()
        }

        login_btn.setOnClickListener {
            //send confirm email
            var intent = Intent()
            intent.setClass(
                this.applicationContext,
                MainActivity().javaClass
            )
            startActivity(intent);
        }
    }
}