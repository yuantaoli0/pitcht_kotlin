package com.example.test.ui.dialog.impl

import android.app.Activity
import com.example.test.R
import com.example.test.ui.dialog.BaseDialog
import kotlinx.android.synthetic.main.modify_dialog.*

class ModifyDialog(activity: Activity, layoutId: Int) : BaseDialog(activity, layoutId) {

    override fun initEvent() {
        btn_done.setOnClickListener {
            this.dismiss()
        }
        tv_modify_user.setOnClickListener {
            activity?.let { it1 -> ModifyUsernameDialog(activity, R.layout.modify_username_dialog).show() }
        }

        tv_modify_email.setOnClickListener {
            activity?.let { it1 -> ModifyEmailDialog(activity,R.layout.modify_email_dialog).show() }
        }

        tv_modify_password.setOnClickListener {
            activity?.let { it1 -> ModifyPasswordDialog(activity,R.layout.modify_password_dialog).show() }
        }

        tv_modify_2fa.setOnClickListener {
            activity?.let {
                    it1 -> Modify2faDialog(activity,R.layout.modify_2fa_dialog).show()
            }
        }

    }

    override fun initView() {

    }
}