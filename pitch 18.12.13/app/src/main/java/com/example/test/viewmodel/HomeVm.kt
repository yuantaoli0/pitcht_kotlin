package com.example.test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test.api.Api
import com.example.test.entity.User
import com.example.test.entry.RegisterEntry
import com.example.test.entry.UserEntry
import com.example.test.ui.fragment.ProfileFragment
import com.example.test.utils.RequestBodyUtil
import com.feng.kotlin.AppContext
import com.lsx.network.ApiResult
import com.rczs.gis.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeVm : BaseViewModel() {

    val userEntry by lazy {
        MutableLiveData<UserEntry>()
    }

    val registerEntry by lazy {
        MutableLiveData<RegisterEntry>()
    }

    fun getUserInfo(token: String) {
        viewModelScope.launch {
            when (val result = Api.retrofitService.getUserInfo("Bearer "+token)) {
                is ApiResult.Success -> {
                    result.data.let {
                        userEntry.value = it
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                }
            }
        }
    }

    fun updatePassword(token: String,oldPassword:String,newPassword:String){
        val hashMap = HashMap<String, Any>().apply {
            this["oldpassword"]= oldPassword
            this["newpassword"]= newPassword
        }
        val requestBody = RequestBodyUtil.getRequestBody(hashMap)
        viewModelScope.launch {
            when (val result = Api.retrofitService.updatePassword("Bearer "+token,requestBody)) {
                is ApiResult.Success -> {
                    result.data.let {
                        registerEntry.value = it
                        toastStr.value= it.toString()
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                }
            }
        }
    }

    fun updateEmail(token: String,email:String){
        val hashMap = HashMap<String, Any>().apply {
            this["email"]= email
        }
        val requestBody = RequestBodyUtil.getRequestBody(hashMap)
        viewModelScope.launch {
            when (val result = Api.retrofitService.updateEmail("Bearer "+token,requestBody)) {
                is ApiResult.Success -> {
                    result.data.let {
                        registerEntry.value = it
                        toastStr.value= it.toString()
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                }
            }
        }
    }

    fun updateUsername(token: String,username:String){
        val hashMap = HashMap<String, Any>().apply {
            this["username"]= username
        }
        val requestBody = RequestBodyUtil.getRequestBody(hashMap)
        viewModelScope.launch {
            when (val result = Api.retrofitService.updateUsername("Bearer "+token,requestBody)) {
                is ApiResult.Success -> {
                    result.data.let {
                        AppContext.user?.username = username
                        registerEntry.value = it
                        toastStr.value=it.toString()
                        ProfileFragment.refresh()
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                }
            }
        }
    }

    fun updateUserInfo(token: String,user: User) {
        val hashMap = HashMap<String, Any>().apply {
            this["email"]= user.email.toString()
            this["username"]= user.username.toString()
        }
        val requestBody = RequestBodyUtil.getRequestBody(hashMap)
        viewModelScope.launch {
            when (val result = Api.retrofitService.upadteUserInfo("Bearer "+token,requestBody)) {
                is ApiResult.Success -> {
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
