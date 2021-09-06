package com.example.test.ui.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.entity.User
import com.example.test.service.AppDataBase
import com.example.test.ui.ForgetActivity
import com.example.test.ui.HomeActivity
import com.feng.kotlin.AppContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    var mAuth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient?=null
    val RC_SIGN_IN = 9001
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()

        tv_create_account.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        login_btn.setOnClickListener {
            if (et_email.text.toString().isEmpty()) {
                Toast.makeText(activity, "email not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            if (et_password.text.toString().isEmpty()) {
                Toast.makeText(activity, "password not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            val users: List<User> = AppDataBase.getDBInstace().getUserDao()
                .getUserByEmailAndPassword(
                    et_email.text.toString().trim(),
                    et_password.text.toString().trim()
                );
            if (users != null && users.size == 1) {
                var intent = Intent()
                activity?.let { it1 ->
                    intent.setClass(
                        it1.applicationContext,
                        HomeActivity().javaClass
                    )
                }
                AppContext.user = users.get(0);
                startActivity(intent);
                activity?.finish();
            } else {
                Toast.makeText(activity, "Email or Password is wrong", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
        }

       iv_google.setOnClickListener {
            val signInIntent: Intent? = mGoogleSignInClient?.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        tv_forget.setOnClickListener {
            var intent = Intent()
            activity?.let { it1 ->
                intent.setClass(
                    it1.applicationContext,
                    ForgetActivity().javaClass
                )
            }
            startActivity(intent);
        }
    }

    fun initData(){
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

       mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }

        et_password.setOnTouchListener(OnTouchListener { v, event ->
            val drawable: Drawable =
                et_password.getCompoundDrawables().get(2) ?: return@OnTouchListener false
            if (event.action != MotionEvent.ACTION_UP) return@OnTouchListener false
            if (event.x > (et_password.getWidth()
                        - et_password.getPaddingRight()
                        - drawable.intrinsicWidth)
            ) {
                if(et_password.getCompoundDrawables().get(2).constantState?.equals(resources.getDrawable(R.mipmap.eye_close).constantState) == true){
                    var left = resources.getDrawable(R.mipmap.password)
                    left.setBounds(0, 0, left.getMinimumWidth(),left.getMinimumHeight())

                    var right = resources.getDrawable(R.mipmap.eye)
                    right.setBounds(0, 0, right.getMinimumWidth(),right.getMinimumHeight())
                    et_password.setCompoundDrawables(left,null,right,null);
                    et_password.inputType = 0x00000081
                }else{
                    var left = resources.getDrawable(R.mipmap.password)
                    left.setBounds(0, 0, left.getMinimumWidth(),left.getMinimumHeight())

                    var right = resources.getDrawable(R.mipmap.eye_close)
                    right.setBounds(0, 0, right.getMinimumWidth(),right.getMinimumHeight())
                    et_password.setCompoundDrawables(left,null,right,null);
                    et_password.inputType = 0x00000001
                }
            }
            false
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                val account = task.getResult(ApiException::class.java)
                val user:User = User(0,account.displayName,account.email,"");
                AppContext.user = user;
                Log.i("TAG",account.toString())

                var intent = Intent()
                activity?.let { it1 ->
                    intent.setClass(
                        it1.applicationContext,
                        HomeActivity().javaClass
                    )
                }
                startActivity(intent);
                activity?.finish();
                // Signed in successfully, show authenticated UI.
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}