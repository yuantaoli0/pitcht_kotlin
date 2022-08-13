package com.example.test.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.viewmodel.ForgetVm
import com.feng.kotlin.AppContext
import com.rczs.gis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetActivity : BaseActivity<ForgetVm>() {
    fun initEvent() {
        confirm_btn.setOnClickListener {
            if(et_email.text.trim().isEmpty()){
                Toast.makeText(this, "Email not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            viewModel.askRecovery(et_email.text.trim().toString())
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        tv_cancle.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setResLayout(): Int {
        return R.layout.activity_forget_password
    }

    override fun initData() {
        initEvent()
    }

    override fun initView() {
        et_email.setText(AppContext.user?.email);
    }

    override fun liveDataObserve() {
        viewModel.recoveryEntry.observe(this){
            var intent = Intent()
            intent.setClass(
                this.applicationContext,
                ConfirmEditActivity().javaClass
            )
            startActivity(intent);
        }
    }
}