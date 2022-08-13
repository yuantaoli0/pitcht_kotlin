package com.example.test.utils

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author win_lsx
 */
object RequestBodyUtil {
    fun getRequestBody(map: HashMap<String, Any>): RequestBody {
        return RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), Gson().toJson(map))
    }

    fun getUploadFileBody(file:File): MultipartBody.Part {
        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return fileToUpload
    }

}