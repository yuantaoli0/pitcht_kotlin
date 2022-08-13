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

class ForgetVm : BaseViewModel() {

    val recoveryEntry by lazy {
        MutableLiveData<RegisterEntry>()
    }

    fun askRecovery(email: String) {
        viewModelScope.launch {
            val hashMap = HashMap<String, Any>().apply {
                this["email"]= email
            }
            val requestBody = RequestBodyUtil.getRequestBody(hashMap)
            when (val result = Api.retrofitService.askRecovery(requestBody)) {
                is ApiResult.Success -> {
                    toastStr.value="email send success"
                    result.data.let {
                        recoveryEntry.value = it
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                }
            }
        }
    }




}
