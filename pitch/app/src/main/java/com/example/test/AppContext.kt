package com.feng.kotlin

import android.app.Application
import com.example.test.entity.User


class AppContext : Application() {
    companion object {
        private  var instance: Application? = null;
        var user: User? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}