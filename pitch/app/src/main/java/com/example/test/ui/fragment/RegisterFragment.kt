package com.example.test.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.test.R
import com.example.test.entity.User
import com.example.test.service.AppDataBase
import com.example.test.ui.ForgetActivity
import com.example.test.ui.ProfileActivity
import com.feng.kotlin.AppContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    var mAuth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient?=null
    val RC_SIGN_IN = 9001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
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

            if(et_password.text.toString().isEmpty()){
                Toast.makeText(activity, "password not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            if(!et_password.text.toString().equals(et_confirm_password.text.toString().trim())){
                Toast.makeText(activity, "the confirm password is wrong", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            //select user is exsits
            var existUser = AppDataBase.getDBInstace().getUserDao().getUserByName(et_name.text.toString().trim());
            if(existUser!=null && existUser.size>0){
                Toast.makeText(activity, "the user is exists", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            var user = User(0,et_name.text.toString().trim(),
                et_email.text.toString().trim(),et_password.text.toString().trim());

            AppDataBase.getDBInstace().getUserDao().insert(user);
            Toast.makeText(activity, "register success", Toast.LENGTH_SHORT).show();
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
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

    fun initData(){
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
                val user:User = User(0,account.displayName,account.email,"");
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
}