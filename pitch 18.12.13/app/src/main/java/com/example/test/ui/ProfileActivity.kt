package com.example.test.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.feng.kotlin.AppContext
import com.google.android.gms.common.util.IOUtils.copyStream
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.profile_icon
import kotlinx.android.synthetic.main.activity_profile_edit.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.*


class ProfileActivity : AppCompatActivity() {
    private var personalImagePath:String?=null
    private var personalImageDir:String?=null

    private var uri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        initData()
        initEvent()
    }

    fun initData(){
        personalImagePath = "/pitch/"+AppContext.user?.username+"/profile.jpg"
        personalImageDir = "/pitch/"+AppContext.user?.username
        var file:File = File(externalCacheDir,personalImagePath);
        var dir:File = File(externalCacheDir,personalImageDir);
        if(file.exists()){
            profile_icon.setImageBitmap(getLoacalBitmap(externalCacheDir?.absolutePath+File.separator+personalImagePath))
        }else{
            dir.mkdirs()
        }
    }

    fun initEvent(){
        tv_modify.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 0x001)
        }

        profile_icon.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 0x001)
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
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            uri = data!!.data
            //copy
            var file:File = File(externalCacheDir,personalImagePath)
            if(!file.exists()){
                file.createNewFile()
            }
            uri?.let { copyFile(this, it,file) }
            profile_icon.setImageBitmap(getLoacalBitmap(externalCacheDir?.absolutePath+File.separator+personalImagePath))
        }
    }

    fun  copyFile(context: Context, srcUri:Uri, dstFile:File) {
        try {
             val inputStream: InputStream? = context.getContentResolver().openInputStream(srcUri)
            if (inputStream == null) return;
            val outputStream : OutputStream = FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (e:Exception) {
            e.printStackTrace();
        }
    }

    @Override
    fun onDestory(){
        EventBus.getDefault().unregister(this);
    }
}