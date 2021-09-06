package com.example.test.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.ui.EditActivity
import com.example.test.ui.HomeActivity
import com.feng.kotlin.AppContext
import com.google.android.gms.common.util.IOUtils
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.*

class ProfileFragment: BaseFragment() {
    private var personalImagePath:String?=null
    private var personalImageDir:String?=null
    private var uri: Uri?=null

    override fun getLayout(): Int {
        return R.layout.activity_profile
    }

    override fun initView(view:View){
        personalImagePath = "/pitch/"+ AppContext.user?.userName+"/profile.jpg"
        personalImageDir = "/pitch/"+ AppContext.user?.userName
        var file: File = File(activity?.externalCacheDir,personalImagePath);
        var dir: File = File(activity?.externalCacheDir,personalImageDir);
        if(file.exists()){
            profile_icon.setImageBitmap(getLoacalBitmap(activity?.externalCacheDir?.absolutePath+ File.separator+personalImagePath))
        }else{
            dir.mkdirs()
        }
    }

    override fun initEvent() {
        iv_edit.setOnClickListener {
            var intent = Intent()
            activity?.let { it1 ->
                intent.setClass(
                    it1.applicationContext,
                    EditActivity().javaClass
                )
            }
            startActivity(intent);
        }
    }

    fun getLoacalBitmap(url: String?): Bitmap? {
        return try {
            val fis = FileInputStream(url)
            BitmapFactory.decodeStream(fis) ///把流转化为Bitmap图片
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

}