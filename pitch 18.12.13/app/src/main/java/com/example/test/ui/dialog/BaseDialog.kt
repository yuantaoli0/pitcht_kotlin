package com.example.test.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.example.test.utils.Utils
import com.rczs.gis.base.BaseViewModel
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext
import java.lang.reflect.ParameterizedType

open abstract class BaseDialog(var activity: Activity, val layoutId:Int) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setCanceledOnTouchOutside(false)
        afterInitView()
        initView()
        initEvent()
    }

    open fun initData(){

    }

    fun afterInitView(){
        val layoutParams = window?.attributes
        layoutParams?.apply {
            width = Utils.dp2px(context.resources,300.0f).toInt()
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }
        window?.attributes = layoutParams
    }

    open abstract fun initView()

    open abstract fun initEvent()
}