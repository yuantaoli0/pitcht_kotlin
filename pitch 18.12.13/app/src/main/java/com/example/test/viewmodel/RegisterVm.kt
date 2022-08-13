package com.example.test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test.api.Api
import com.example.test.entity.User
import com.example.test.entry.LoginEntry
import com.example.test.entry.RegisterEntry
import com.example.test.utils.RequestBodyUtil
import com.lsx.network.ApiResult
import com.rczs.gis.base.BaseViewModel
import kotlinx.coroutines.launch

class RegisterVm : BaseViewModel() {
    val registerEntry by lazy {
        MutableLiveData<RegisterEntry>()
    }
    fun register(user: User) {
        viewModelScope.launch {
            val hashMap = HashMap<String, Any>().apply {
                this["email"]= user.email.toString()
                this["password"]= user.password.toString()
                this["username"]= user.username.toString()
            }
            val requestBody = RequestBodyUtil.getRequestBody(hashMap)
            when (val result = Api.retrofitService.register(requestBody)) {
                is ApiResult.Success -> {
                    toastStr.value= result.data.message
                    result.data.let {
                        registerEntry.value = it
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                }
            }
        }
    }




}
