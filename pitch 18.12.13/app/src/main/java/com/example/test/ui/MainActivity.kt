package com.example.test.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .permission(Permission.Group.CAMERA)
            .permission(Permission.Group.MICROPHONE)
            .onGranted { permissions ->

            }.onDenied { permissions -> }.start()
    }
}