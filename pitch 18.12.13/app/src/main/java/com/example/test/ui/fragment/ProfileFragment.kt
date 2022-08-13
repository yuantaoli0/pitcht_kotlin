package com.example.test.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Editable
import android.view.View
import android.widget.TextView
import com.example.test.R
import com.example.test.ui.EditActivity
import com.example.test.ui.dialog.impl.ModifyDialog
import com.feng.kotlin.AppContext
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ProfileFragment: BaseFragment() {
    private var personalImagePath: String? = null
    private var personalImageDir: String? = null
    private var uri: Uri? = null

    companion object {
        private var nameTv : TextView?=null
        // 包裹范围内 属于静态方法
        fun refresh(){
            nameTv?.text = Editable.Factory.getInstance().newEditable(AppContext.user?.username ?: "");
        }

    }

    override fun getLayout(): Int {
        return R.layout.activity_profile
    }

    override fun initView(view:View){
        personalImagePath = "/pitch/"+ AppContext.user?.username+"/profile.jpg"
        personalImageDir = "/pitch/"+ AppContext.user?.username
        var file: File = File(activity?.externalCacheDir,personalImagePath);
        var dir: File = File(activity?.externalCacheDir,personalImageDir);
        if(file.exists()){
            profile_icon.setImageBitmap(getLoacalBitmap(activity?.externalCacheDir?.absolutePath+ File.separator+personalImagePath))
        }else{
            dir.mkdirs()
        }

        tv_name.text = Editable.Factory.getInstance().newEditable(AppContext.user?.username ?: "");
        nameTv = tv_name
        EventBus.getDefault().register(this);
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

        tv_modify_user.setOnClickListener{
            activity?.let { it1 -> ModifyDialog(activity!!,R.layout.modify_dialog).show() }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(msg:String) {
        if(msg.equals("refreshProfile")){
            personalImagePath = "/pitch/"+ AppContext.user?.username+"/profile.jpg"
            personalImageDir = "/pitch/"+ AppContext.user?.username
            var file: File = File(activity?.externalCacheDir,personalImagePath);
            var dir: File = File(activity?.externalCacheDir,personalImageDir);
            if(file.exists()){
                profile_icon.setImageBitmap(getLoacalBitmap(activity?.externalCacheDir?.absolutePath+ File.separator+personalImagePath))
            }else{
                dir.mkdirs()
            }
        }

        if(msg.equals("refreshName")){
            tv_name.text = Editable.Factory.getInstance().newEditable(AppContext.user?.username ?: "");
        }
    }

    @Override
    fun onDestory(){
        EventBus.getDefault().unregister(this);
    }
}