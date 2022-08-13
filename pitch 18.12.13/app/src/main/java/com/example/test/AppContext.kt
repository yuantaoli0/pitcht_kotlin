package com.feng.kotlin

import android.app.Application
import com.example.test.entity.User
import com.google.firebase.database.FirebaseDatabase

class AppContext : Application() {
    companion object {
        private  var instance: Application? = null;
        var user: User? = null
        var token : String= ""
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}