package com.example.test.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test.api.Api
import com.example.test.entity.User
import com.example.test.entry.LoginEntry
import com.example.test.utils.RequestBodyUtil
import com.lsx.network.ApiResult
import com.rczs.gis.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginVm : BaseViewModel() {
    var TAG = LoginVm::javaClass.name
    val loginEntry by lazy {
        MutableLiveData<LoginEntry>()
    }

    fun login(user: User) {
        viewModelScope.launch {
            val hashMap = HashMap<String, Any>().apply {
                this["email"]= user.email.toString()
                this["password"]= user.password.toString()
                this["tfa"]= ""

            }
            val requestBody = RequestBodyUtil.getRequestBody(hashMap)
            when (val result = Api.retrofitService.login(requestBody)) {
                is ApiResult.Success -> {
                    toastStr.value="login success"
                    result.data.let {
                        loginEntry.value = it
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                    Log.i(TAG,result.errorMsg);
                }
            }
        }
    }

}
