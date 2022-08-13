package com.example.test.ui.fragment

import android.content.Intent
import android.view.View
import android.widget.GridView
import android.widget.LinearLayout
import com.example.test.R
import com.example.test.adapter.VideoDispalyAdapter
import com.example.test.dto.VideoDisplayDto
import com.example.test.ui.MpChartsActivity
import com.example.test.utils.Utils
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment: BaseFragment() {
    private var videoList:MutableList<VideoDisplayDto> = mutableListOf(VideoDisplayDto("name1",R.mipmap.top_icon),
        VideoDisplayDto("name2",R.mipmap.top_icon),VideoDisplayDto("name2",R.mipmap.top_icon),
        VideoDisplayDto("name3",R.mipmap.top_icon),VideoDisplayDto("name4",R.mipmap.top_icon)
    )

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initEvent() {
        super.initEvent()
        iv_bottom_icon.setOnClickListener {
            startActivity(Intent(activity,MpChartsActivity::class.java))
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        gv_videos.horizontalSpacing=5
        gv_videos.numColumns=videoList.size
        var cloumnWidth = getResources().getDisplayMetrics().widthPixels/2
        gv_videos.columnWidth=cloumnWidth
        gv_videos.stretchMode = GridView.NO_STRETCH
        val itemPaddingH: Float = Utils.dp2px(getResources(), 5f)
        gv_videos.horizontalSpacing = itemPaddingH.toInt()
        val gridviewWidth: Int = videoList.size * (cloumnWidth + itemPaddingH.toInt())
        val params = LinearLayout.LayoutParams(
            gridviewWidth, cloumnWidth*3/5
        )
        gv_videos.setLayoutParams(params)
        var sim_adapter = context?.let { VideoDispalyAdapter(it, videoList as ArrayList<VideoDisplayDto>) }
        //配置适配器
        gv_videos.adapter = sim_adapter

        //初始化图表图片大小
        val params1 = LinearLayout.LayoutParams(
            cloumnWidth*4/3, cloumnWidth
        )
        iv_bottom_icon.setLayoutParams(params1)
    }
}