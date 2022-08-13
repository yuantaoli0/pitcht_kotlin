package com.lsx.network

import com.lsx.network.calladapter.ApiResultCallAdapterFactory
import com.lsx.network.interceptor.BusinessErrorInterceptor
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://pitchit.studio/"

private val cookieStore: HashMap<String, List<Cookie>> = HashMap()

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(BusinessErrorInterceptor())
    .cookieJar(object :CookieJar{
        override fun saveFromResponse(httpUrl: HttpUrl, list:List<Cookie>) {
            cookieStore.put(BASE_URL, list);
        }

        override fun loadForRequest(httpUrl:HttpUrl): List<Cookie>  {
            val cookies = cookieStore[httpUrl.host()]
            return cookies ?: ArrayList()
        }
    })
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .addCallAdapterFactory(ApiResultCallAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()
