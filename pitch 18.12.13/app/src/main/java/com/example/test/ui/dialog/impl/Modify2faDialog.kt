package com.example.test.ui.dialog.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.TextUtils
import android.widget.Toast
import com.example.test.R
import com.example.test.entity.User
import com.example.test.twofa.Enable2FAdialog
import com.example.test.twofa.Statics
import com.example.test.ui.HomeActivity
import com.example.test.ui.dialog.BaseDialog
import com.example.test.ui.fragment.ProfileFragment
import com.feng.kotlin.AppContext
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile_edit.iv_back
import kotlinx.android.synthetic.main.modify_dialog.btn_done
import kotlinx.android.synthetic.main.modify_username_dialog.*

class Modify2faDialog(context: Activity, layoutId: Int) : BaseDialog(context, layoutId) {

    override fun initView() {
    }

    override fun initEvent() {
        Statics.auth.signInWithEmailAndPassword(
            AppContext.user?.username,
            AppContext.user?.password
        )
            .addOnSuccessListener(activity, OnSuccessListener<AuthResult?> {
                Statics.usersTable.child(FirebaseAuth.getInstance().currentUser.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val twoFactorAuthOn: Boolean =
                                dataSnapshot.getValue(User::class.java)?.twoFactorAuthOn == true
                            /** if user has activated two factor authentication  */
                            /** if user has activated two factor authentication  */
                            if (twoFactorAuthOn) {
                                val loggedUserPrefs: SharedPreferences =
                                    activity!!.getSharedPreferences("2FA", 0)
                                val e = loggedUserPrefs.edit()
                                e.putString("status", "unfinished")
                                e.commit()
                                Enable2FAdialog(activity, R.style.TwoFADialogs).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            throw databaseError.toException()
                        }
                    })
            }
            )
    }
}