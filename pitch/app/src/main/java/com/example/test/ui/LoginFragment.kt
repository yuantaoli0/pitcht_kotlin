package com.example.test.ui

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
import com.example.test.databinding.FragmentLoginBinding
import com.example.test.entity.User
import com.example.test.service.AppDataBase
import com.feng.kotlin.AppContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var mAuth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient?=null
    val RC_SIGN_IN = 9001
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        initData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.loginBtn.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty()) {
                Toast.makeText(activity, "email not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            if (binding.etPassword.text.toString().isEmpty()) {
                Toast.makeText(activity, "password not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            val users: List<User> = AppDataBase.getDBInstace().getUserDao()
                .getUserByEmailAndPassword(
                    binding.etEmail.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                );
            if (users != null && users.size == 1) {
                var intent = Intent()
                activity?.let { it1 ->
                    intent.setClass(
                        it1.applicationContext,
                        ProfileActivity().javaClass
                    )
                }
                startActivity(intent);
                activity?.finish();
            } else {
                Toast.makeText(activity, "Email or Password is wrong", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
        }

        binding.ivGoogle.setOnClickListener {
            val signInIntent: Intent? = mGoogleSignInClient?.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)

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
        _binding = null
    }
}