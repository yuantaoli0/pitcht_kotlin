package com.example.test.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.android.volley.Response
import com.example.micosoft.MSGraphRequestWrapper
import com.example.test.R
import com.example.test.entity.User
import com.example.test.service.AppDataBase
import com.example.test.twofa.Enable2FAdialog
import com.example.test.twofa.Statics
import com.example.test.ui.ForgetActivity
import com.example.test.ui.HomeActivity
import com.example.test.utils.SPUtils
import com.example.test.viewmodel.LoginVm
import com.feng.kotlin.AppContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.microsoft.identity.client.*
import com.microsoft.identity.client.IPublicClientApplication.ISingleAccountApplicationCreatedListener
import com.microsoft.identity.client.ISingleAccountPublicClientApplication.CurrentAccountCallback
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalException
import com.microsoft.identity.client.exception.MsalServiceException
import com.microsoft.identity.client.exception.MsalUiRequiredException
import com.rczs.gis.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : BaseFragment<LoginVm>() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    var mAuth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    val RC_SIGN_IN = 9001
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
    private var mAccount: IAccount? = null
    val defaultGraphResourceUrl = MSGraphRequestWrapper.MS_GRAPH_ROOT_ENDPOINT + "v1.0/me"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createSingleAccountPublicClientApplication(
            requireContext(),
            R.raw.auth_config_single_account,
            object : ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    /**
                     * This test app assumes that the app is only going to support one account.
                     * This requires "account_mode" : "SINGLE" in the config json file.
                     */
                    mSingleAccountApp = application
                    loadAccount()
                }

                override fun onError(exception: MsalException) {
                    exception.printStackTrace()
                }
            })

    }

    override fun initData() {
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
                if (et_password.getCompoundDrawables()
                        .get(2).constantState?.equals(resources.getDrawable(R.mipmap.eye_close).constantState) == true
                ) {
                    var left = resources.getDrawable(R.mipmap.password)
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight())

                    var right = resources.getDrawable(R.mipmap.eye)
                    right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight())
                    et_password.setCompoundDrawables(left, null, right, null);
                    et_password.inputType = 0x00000081
                } else {
                    var left = resources.getDrawable(R.mipmap.password)
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight())

                    var right = resources.getDrawable(R.mipmap.eye_close)
                    right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight())
                    et_password.setCompoundDrawables(left, null, right, null);
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
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                val account = task.getResult(ApiException::class.java)
                val user: User =
                    User(0, account.displayName, account.displayName, account.email, "", "", false,
                        "","","");
                AppContext.user = user
                Log.i("TAG", account.toString())

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

    override fun setResLayout(): Int {
        return R.layout.fragment_login
    }

    override fun initView(savedInstanceState: Bundle?) {

        tv_create_account.setOnClickListener {
            findNavController(tv_create_account).navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        home_btn.setOnClickListener {
            startActivity(Intent(context, HomeActivity::class.java))
            activity?.finish()
        }

        //登录点击事件
        login_btn.setOnClickListener {
            if (et_email.text.toString().isEmpty()) {
                Toast.makeText(activity, "email not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            if (et_password.text.toString().isEmpty()) {
                Toast.makeText(activity, "password not allow empty", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            //loginLocalbase()
            val user = User(
                0,
                "",
                "",
                et_email.text.toString(),
                et_password.text.toString(),
                "",
                false,
                "",
                "",
                ""
            )

            AppContext.user = user

            //自已平台api接口
            viewModel.login(user)
        }

        //谷歌一键登录
        iv_google.setOnClickListener {
            val signInIntent: Intent? = mGoogleSignInClient?.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        //微软一键登录
        iv_microsoft.setOnClickListener {
            if (mSingleAccountApp == null) {
                return@setOnClickListener
            }

            getAuthSilentCallback()?.let { it1 ->
                mSingleAccountApp!!.signIn(
                    requireActivity(), null,
                    arrayOf("user.readwrite"), it1
                )
            }
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

    fun loginLocalbase() {
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
        }
    }

    override fun liveDataObserve() {
        viewModel.loginEntry.observe(this) {

            //登录成功之后把token放在全局变量中
            AppContext.token = it.token
            Enable2FAdialog(activity, R.style.TwoFADialogs).show()
            return@observe
            var intent = Intent()
            activity?.let { it1 ->
                intent.setClass(
                    it1.applicationContext,
                    HomeActivity().javaClass
                )
            }
            startActivity(intent)
            activity?.finish()
            return@observe
            twofaConfirm()
        }
    }

    /**
     * Load the currently signed-in account, if there's any.
     */
    private fun loadAccount() {
        if (mSingleAccountApp == null) {
            return
        }
        mSingleAccountApp!!.getCurrentAccountAsync(object : CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                // You can use the account data to update your UI or your app database.
                mAccount = activeAccount
                updateUI()
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                if (currentAccount == null) {
                }
            }

            override fun onError(exception: MsalException) {
            }
        })
    }

    //微软登录回调
    private fun getAuthSilentCallback(): AuthenticationCallback? {
        return object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {

                mAccount = authenticationResult.account
                callGraphAPI(authenticationResult)
                updateUI()
            }

            override fun onError(exception: MsalException) {
                if (exception is MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception is MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception is MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }

            override fun onCancel() {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        /**
         * The account may have been removed from the device (if broker is in use).
         *
         * In shared device mode, the account might be signed in/out by other apps while this app is not in focus.
         * Therefore, we want to update the account state by invoking loadAccount() here.
         */
        loadAccount()
    }


    /**
     * Make an HTTP request to obtain MSGraph data
     */
    private fun callGraphAPI(authenticationResult: IAuthenticationResult) {
        MSGraphRequestWrapper.callGraphAPIUsingVolley(
            requireContext(),
            defaultGraphResourceUrl,
            authenticationResult.accessToken,
            Response.Listener<JSONObject> { response -> /* Successfully called graph, process data and send to UI */
                Toast.makeText(context, "login success", Toast.LENGTH_LONG).show()
                val user: User = User(
                    0, response.getString("displayName"), response.getString("displayName"),
                    response.getString("userPrincipalName"), "", "", false,"","",""
                );
                AppContext.user = user
                context?.let { SPUtils.put(it, "loginUser", Gson().toJson(user)) }
                var intent = Intent()
                activity?.let { it1 ->
                    intent.setClass(
                        it1.applicationContext,
                        HomeActivity().javaClass
                    )
                }
                startActivity(intent)
                activity?.finish()
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "login failed", Toast.LENGTH_LONG).show()
            })
    }

    /**
     * Updates UI based on the current account.
     */
    private fun updateUI() {
        if (mAccount != null) {
            var user = context?.let { SPUtils.get(it, "loginUser", "") }
            if (!TextUtils.isEmpty(user)) {
                AppContext.user = Gson().fromJson(user, User::class.java)
                var intent = Intent()
                activity?.let { it1 ->
                    intent.setClass(
                        it1.applicationContext,
                        HomeActivity().javaClass
                    )
                }
                startActivity(intent)
                activity?.finish()
                return
                twofaConfirm()
            }

        } else {

        }
    }

    //2fa验证
    private fun twofaConfirm(){
        try{
            //判断是否开发2fa
            activity?.let {
                Statics.auth.signInWithEmailAndPassword(
                    AppContext.user?.email,
                    AppContext.user?.password
                )
                    .addOnSuccessListener(it, OnSuccessListener<AuthResult?> {
                        Log.i("2fa",Gson().toJson(FirebaseAuth.getInstance().currentUser))
                        Statics.usersTable.child(FirebaseAuth.getInstance().currentUser.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val twoFactorAuthOn: Boolean =
                                        dataSnapshot.getValue(User::class.java)?.twoFactorAuthOn == true
                                    /** if user has activated two factor authentication  */
                                    if (twoFactorAuthOn) {
                                        val loggedUserPrefs: SharedPreferences =
                                            activity!!.getSharedPreferences("2FA", 0)
                                        val e = loggedUserPrefs.edit()
                                        e.putString("status", "unfinished")
                                        e.commit()
                                        Enable2FAdialog(activity, R.style.TwoFADialogs).show()
                                    }
                                    /** if user hasnt activated two factor authentication just   */
                                    if (!twoFactorAuthOn) {
                                        val loggedUserPrefs: SharedPreferences =
                                            activity!!.getSharedPreferences("2FA", 0)
                                        val e = loggedUserPrefs.edit()
                                        e.putString("status", "finished")
                                        e.commit()

                                        var intent = Intent()
                                        activity?.let { it1 ->
                                            intent.setClass(
                                                it1.applicationContext,
                                                HomeActivity().javaClass
                                            )
                                        }
                                        startActivity(intent)
                                        activity?.finish()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    throw databaseError.toException()
                                }
                            })
                    }
                    )
                    .addOnCompleteListener(
                        requireActivity(),
                        OnCompleteListener<AuthResult?> { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    activity,
                                    task.exception!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }//
                        })
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}