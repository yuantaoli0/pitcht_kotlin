package com.example.test.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test.api.Api
import com.example.test.entry.UploadEntry
import com.example.test.utils.RequestBodyUtil
import com.lsx.network.ApiResult
import com.rczs.gis.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadVm : BaseViewModel() {

    val uploadEntry by lazy {
        MutableLiveData<UploadEntry>()
    }

    fun uploadFile(token: String,file: File) {
        viewModelScope.launch {
            val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)
            var multipartBody = RequestBodyUtil.getUploadFileBody(file)
            when (val result = Api.retrofitService.uploadFile("Bearer "+token,multipartBody,filename)) {
                is ApiResult.Success -> {
                    result.data.let {
                        uploadEntry.value = it
                    }
                }
                is ApiResult.Failure -> {
                    toastStr.value=result.errorMsg
                }
            }
        }
    }

}
