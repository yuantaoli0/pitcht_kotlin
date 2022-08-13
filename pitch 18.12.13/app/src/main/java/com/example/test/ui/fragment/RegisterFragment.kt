package com.example.test.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.entity.User
import com.example.test.service.AppDataBase
import com.example.test.ui.ForgetActivity
import com.example.test.ui.HomeActivity
import com.example.test.ui.ProfileActivity
import com.example.test.viewmodel.LoginVm
import com.example.test.viewmodel.RegisterVm
import com.feng.kotlin.AppContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.rczs.gis.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.et_email
import kotlinx.android.synthetic.main.fragment_register.et_password
import kotlinx.android.synthetic.main.fragment_register.iv_google
import kotlinx.android.synthetic.main.fragment_register.tv_forget
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : BaseFragment<RegisterVm>() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    var mAuth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient?=null
    val RC_SIGN_IN = 9001
    var p: Pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initData(){
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                val account = task.getResult(ApiException::class.java)
                val user:User = User(0,account.displayName,account.displayName,account.email,"","",false,
                    "","","");
                AppContext.user = user;
                Log.i("TAG",account.toString())

                var intent = Intent()
                activity?.let { it1 ->
                    intent.setClass(
                        it1.applicationContext,
                        ProfileActivity().javaClass
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

    //验证函数优化版
    fun isEmail(email: String?): Boolean {
        if (null == email || "" == email) return false
        val m: Matcher = p.matcher(email)
        return m.matches()
    }

    override fun setResLayout(): Int {
        return R.layout.fragment_register
    }

    override fun initView(savedInstanceState: Bundle?) {
        tv_login.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        register_btn.setOnClickListener{
            if(et_name.text.toString().isEmpty()){
                Toast.makeText(activity, "name not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            if(et_email.text.toString().isEmpty()){
                Toast.makeText(activity, "email not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            if(!isEmail(et_email.text.toString())){
                Toast.makeText(activity, "email format is wrong", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            if(et_password.text.toString().isEmpty()){
                Toast.makeText(activity, "password not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

//            if(!et_password.text.toString().equals(et_confirm_password.text.toString().trim())){
//                Toast.makeText(activity, "the confirm password is wrong", Toast.LENGTH_SHORT).show();
//                return@setOnClickListener
//            }

            //select user is exsits
//            registerLocal()
            val user = User(0,"",et_name.text.toString(),et_email.text.toString(),et_password.text.toString(),
            "",false,"","","")
            viewModel.register(user)
        }

        iv_register_back.setOnClickListener {
            onBackPressed()
        }

        iv_google.setOnClickListener{
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

    override fun liveDataObserve() {
        viewModel.registerEntry.observe(this){
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }


    fun registerLocal(){
        var existUser = AppDataBase.getDBInstace().getUserDao().getUserByName(et_name.text.toString().trim());
        if(existUser!=null && existUser.size>0){
            Toast.makeText(activity, "the user is exists", Toast.LENGTH_SHORT).show();
            return
        }

        var user = User(0,et_name.text.toString().trim(),et_name.text.toString().trim(),
            et_email.text.toString().trim(),et_password.text.toString().trim(),"",false,
            "","","")

        AppDataBase.getDBInstace().getUserDao().insert(user);
    }

}