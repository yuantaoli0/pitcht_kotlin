package com.example.test.ui.dialog.impl

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.example.test.ui.HomeActivity
import com.example.test.ui.dialog.BaseDialog
import com.example.test.ui.fragment.ProfileFragment
import com.feng.kotlin.AppContext
import kotlinx.android.synthetic.main.activity_profile_edit.iv_back
import kotlinx.android.synthetic.main.modify_dialog.btn_done
import kotlinx.android.synthetic.main.modify_username_dialog.*

class ModifyUsernameDialog(context: Activity, layoutId: Int) : BaseDialog(context, layoutId) {

    override fun initView() {
    }

    override fun initEvent() {
        btn_done.setOnClickListener {
            if(TextUtils.isEmpty(et_name.text.toString()) ||
                TextUtils.isEmpty(et_confirm_name.text.toString())){
                Toast.makeText(context, "name not allow empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!et_name.text.toString().equals(et_confirm_name.text.toString())){
                Toast.makeText(context, "name is not the same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ((activity as HomeActivity).viewModel.updateUsername(AppContext.token,et_name.text.toString()))
        }

        iv_back.setOnClickListener {
            this.dismiss()
        }
    }
}