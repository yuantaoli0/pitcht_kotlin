package com.example.test.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast


/**
 * @CreateDate:     2021/9/22
 * @Author:         LSX
 * @Description:
 */

/**
 * 显示Toast
 * @receiver Context
 * @param text String
 */
fun Context.showToast(text:String) {
   Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}

/**
 * 隐藏键盘
 * @receiver Activity
 * @param view EditText
 */
fun Activity.hindSoftInput(view: EditText) {
   val imm: InputMethodManager =
           this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
   imm.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 *  显示键盘
 * @receiver Activity
 * @param view EditText
 */
fun Activity.showSoftInput(view: EditText) {
   val imm: InputMethodManager =
           this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
   imm.hideSoftInputFromWindow(view.windowToken, 0)
}