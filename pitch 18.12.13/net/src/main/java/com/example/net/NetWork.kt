package com.lsx.network

import android.annotation.SuppressLint
import android.content.Context


/**
 * @Description:
 * @CreateDate:     2021/7/13
 * @Author:         LSX
 */
@SuppressLint("StaticFieldLeak")
object NetWork {
    lateinit var context: Context
    fun init(ctx: Context) {
        context = ctx.applicationContext
    }
}