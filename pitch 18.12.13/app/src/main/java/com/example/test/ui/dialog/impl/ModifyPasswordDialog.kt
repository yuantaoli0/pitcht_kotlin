package com.example.test.ui.dialog.impl

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.example.test.ui.HomeActivity
import com.example.test.ui.dialog.BaseDialog
import com.example.test.viewmodel.HomeVm
import com.feng.kotlin.AppContext
import kotlinx.android.synthetic.main.activity_profile_edit.*
import kotlinx.android.synthetic.main.activity_profile_edit.iv_back
import kotlinx.android.synthetic.main.modify_dialog.*
import kotlinx.android.synthetic.main.modify_dialog.btn_done
import kotlinx.android.synthetic.main.modify_password_dialog.*
import kotlinx.android.synthetic.main.modify_username_dialog.*

class ModifyPasswordDialog(activity: Activity, layoutId: Int) : BaseDialog(activity, layoutId) {

    private var homeVm: HomeVm?=null

    override fun initView() {
        homeVm = HomeVm()
    }

    override fun initEvent() {
        btn_done.setOnClickListener {
            if(TextUtils.isEmpty(et_password.text.toString()) ||
                TextUtils.isEmpty(et_old_password.text.toString())||
                TextUtils.isEmpty(et_confirm_password.text.toString())){
                Toast.makeText(context, "password not allow empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!et_password.text.toString().equals(et_confirm_password.text.toString())){
                Toast.makeText(context, "password is not the same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ((activity as HomeActivity).viewModel.updatePassword(AppContext.token, et_old_password.text.toString(),
                et_password.text.toString()))
        }

        iv_back.setOnClickListener {
            this.dismiss()
        }
    }
}