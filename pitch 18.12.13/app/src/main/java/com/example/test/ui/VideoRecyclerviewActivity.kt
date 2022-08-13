package com.example.test.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.adapter.VideoAdapter
import com.example.test.entity.Video
import com.example.test.service.AppDataBase
import com.example.test.viewmodel.EmptyVm
import com.rczs.gis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_video.*
import me.bzcoder.mediapicker.SmartMediaPicker
import me.bzcoder.mediapicker.config.MediaPickerEnum
import android.os.Build

import android.os.LocaleList
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.test.dto.AudioDto
import com.example.test.dto.VideoDto
import com.example.test.viewmodel.UploadVm
import com.feng.kotlin.AppContext
import com.microsoft.graph.models.extensions.File
import kotlinx.android.synthetic.main.activity_audio_list.*
import kotlinx.android.synthetic.main.activity_video.iv_back
import kotlinx.android.synthetic.main.activity_video.ryMessageView
import org.videolan.libvlc.interfaces.IMedia.Type.File
import java.util.*


class VideoRecyclerviewActivity : BaseActivity<UploadVm>() {
    lateinit var adapter: VideoAdapter
    private var state : Boolean =true

    override fun attachBaseContext(newBase: Context?) {
        val res: Resources = newBase!!.getResources()
        val configuration: Configuration = res.getConfiguration()
        var context:Context?=null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(Locale.ENGLISH)
            val localeList = LocaleList(Locale.ENGLISH)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            context = newBase?.createConfigurationContext(configuration)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            configuration.setLocale(Locale.ENGLISH)
            context = newBase?.createConfigurationContext(configuration)
        }
        super.attachBaseContext(context)
    }

    override fun setResLayout(): Int {
        return R.layout.activity_video
    }

    override fun initView() {
        ryMessageView.layoutManager = LinearLayoutManager(applicationContext)
        adapter = VideoAdapter()
        ryMessageView.addItemDecoration(
            DividerItemDecoration(applicationContext,
                LinearLayout.VERTICAL)
        )
        ryMessageView.adapter = adapter
    }

    override fun initData() {
        var list = AppDataBase.getDBInstace().getVideoDao().getAllVideos()
        var voList = ArrayList<VideoDto>()
        for(data in list){
            var temp = VideoDto(data.name!!, data.videoId!!,data.path!!,state,data.length!!,data.date!!)
            voList.add(temp)
        }
        adapter.setList(voList)
        adapter.notifyDataSetChanged()
    }

    override fun initOnClick() {
        super.initOnClick()
        iv_back.setOnClickListener {
            onBackPressed()
        }

        iv_video_edit.setOnClickListener {
            if(state == true){
                iv_video_edit.setImageDrawable(getDrawable(R.mipmap.ic_detail))
                state = false
                initData()
            }else{
                iv_video_edit.setImageDrawable(getDrawable(R.mipmap.ic_edit))
                state = true
                initData()
            }
        }

        iv_upload.setOnClickListener {
            SmartMediaPicker.builder(this) //最大图片选择数目 如果不需要图片 将数目设置为0
                .withMaxImageSelectable(0) //最大视频选择数目 如果不需要视频 将数目设置为0
                .withMaxVideoSelectable(1) //图片选择器是否显示数字
                .withCountable(true)
                .withMaxVideoLength(60 * 1000) //最大视频长度
                .withMaxVideoSize(100) //最大视频文件大小 单位MB
                .withMaxHeight(1920) //最大图片高度 默认1920
                .withMaxWidth(1920) //最大图片宽度 默认1920
                .withMaxImageSize(5) //最大图片大小 单位MB
                .withIsMirror(false) //弹出类别，默认弹出底部选择栏，也可以选择单独跳转
                .withImageEngine(Glide4Engine())//设置图片加载引擎
                .withMediaPickerType(MediaPickerEnum.BOTH)
                .build()
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resultData = SmartMediaPicker.getResultData(this, requestCode, resultCode, data)
        if (resultData != null && resultData.size > 0) {
            resultData.forEach{
                if(AppDataBase.getDBInstace().getVideoDao().getVideoByPath(it)==null){
                    var video = Video(null,it.substring(it.lastIndexOf("/")+1),it,
                        SmartMediaPicker.getVideoDuration(it),0,System.currentTimeMillis())
                    AppDataBase.getDBInstace().getVideoDao().insert(video)

                    //上传视频到服务器
                    val file = java.io.File(it.toString())
                    viewModel.uploadFile(AppContext.token,file)
                }
            }

            //刷新recyclerview
            initData()
        } else {
            Log.i(TAG,"select null")
        }
    }
}