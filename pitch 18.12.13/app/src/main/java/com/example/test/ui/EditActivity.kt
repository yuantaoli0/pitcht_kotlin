package com.example.test.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.test.R
import com.example.test.entity.User
import com.example.test.service.AppDataBase
import com.example.test.viewmodel.HomeVm
import com.feng.kotlin.AppContext
import com.google.android.gms.common.util.IOUtils
import com.rczs.gis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.profile_icon
import kotlinx.android.synthetic.main.activity_profile_edit.*
import kotlinx.android.synthetic.main.activity_profile_edit.tv_name
import kotlinx.android.synthetic.main.fragment_register.*
import org.greenrobot.eventbus.EventBus
import java.io.*

class EditActivity : BaseActivity<HomeVm>(){
    private var personalImagePath:String?=null
    private var personalImageDir:String?=null
    private var uri: Uri?=null

    override fun initView(){
        tv_name.text = Editable.Factory.getInstance().newEditable(AppContext.user?.name ?: "");
        tv_user_name.text = Editable.Factory.getInstance().newEditable(AppContext.user?.username ?: "");
        tv_website_name.text = Editable.Factory.getInstance().newEditable(AppContext.user?.website ?:"");
        tv_desc_name.text = Editable.Factory.getInstance().newEditable(AppContext.user?.desc ?:"");
        personalImagePath = "/pitch/"+ AppContext.user?.username+"/profile.jpg"
        personalImageDir = "/pitch/"+ AppContext.user?.username
        var file: File = File(this?.externalCacheDir,personalImagePath);
        var dir: File = File(this?.externalCacheDir,personalImageDir);
        if(file.exists()){
            profile_icon.setImageBitmap(getLoacalBitmap(this?.externalCacheDir?.absolutePath+ File.separator+personalImagePath))
        }else{
            dir.mkdirs()
        }
    }

    fun initEvent() {
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

        iv_back.setOnClickListener {
            onBackPressed()
        }

        edit_btn.setOnClickListener{
            var user = AppContext.user;
            user?.desc = tv_desc_name.text.toString();
            user?.website = tv_website_name.text.toString();
            user?.name = tv_name.text.toString();
            user?.username = tv_user_name.text.toString();

            if (user != null) {
                AppDataBase.getDBInstace().getUserDao().insert(user)
            };
            finish()
            Toast.makeText(this, "edit success", Toast.LENGTH_SHORT).show();

            EventBus.getDefault().post("refreshName");

            user?.let { it1 -> viewModel.updateUserInfo(AppContext.token,it1) };
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
        if (resultCode == AppCompatActivity.RESULT_OK) {
            uri = data!!.data
            //copy
            var file: File = File(this?.externalCacheDir,personalImagePath)
            if(!file.exists()){
                file.createNewFile()
            }
            uri?.let { this?.let { it1 -> copyFile(it1, it,file) } }
            profile_icon.setImageBitmap(getLoacalBitmap(this?.externalCacheDir?.absolutePath+ File.separator+personalImagePath))

            EventBus.getDefault().post("refreshProfile");
        }
    }

    fun  copyFile(context: Context, srcUri: Uri, dstFile: File) {
        try {
            val inputStream: InputStream? = context.getContentResolver().openInputStream(srcUri)
            if (inputStream == null) return;
            val outputStream : OutputStream = FileOutputStream(dstFile);
            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (e:Exception) {
            e.printStackTrace();
        }
    }

    override fun setResLayout(): Int {
        return R.layout.activity_profile_edit
    }

    override fun initData() {
        initEvent()
    }
}