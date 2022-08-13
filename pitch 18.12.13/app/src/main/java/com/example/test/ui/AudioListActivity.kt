package com.example.test.ui

import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.adapter.AudioAdapter
import com.example.test.dto.AudioDto
import com.example.test.entity.Audio
import com.example.test.service.AppDataBase
import com.example.test.viewmodel.UploadVm
import com.rczs.gis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_audio_list.*
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.activity_video.iv_back
import kotlinx.android.synthetic.main.activity_video.ryMessageView


class AudioListActivity : BaseActivity<UploadVm>() {
    lateinit var adapter: AudioAdapter
    private var state : Boolean =true

    override fun setResLayout(): Int {
        return R.layout.activity_audio_list
    }

    override fun initView() {
        ryMessageView.layoutManager = LinearLayoutManager(applicationContext)
        adapter = AudioAdapter()
        ryMessageView.addItemDecoration(DividerItemDecoration(applicationContext,LinearLayout.VERTICAL))
        ryMessageView.adapter = adapter
    }

    override fun initData() {
        var list = AppDataBase.getDBInstace().getAudioDao().getAllAudios()
        var voList = ArrayList<AudioDto>()
        for(data in list){
            var temp = AudioDto(data.name!!, data.audioId!!,data.path!!,state,data.length!!,data.date!!)
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

        iv_edit.setOnClickListener {
            if(state == true){
                iv_edit.setImageDrawable(getDrawable(R.mipmap.ic_detail))
                state = false
                initData()
            }else{
                iv_edit.setImageDrawable(getDrawable(R.mipmap.ic_edit))
                state = true
                initData()
            }

        }

    }
}