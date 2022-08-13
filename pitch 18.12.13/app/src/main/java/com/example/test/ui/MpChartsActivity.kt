package com.example.test.ui

import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.example.test.R
import com.example.test.adapter.PitchAdapter
import com.example.test.chart.CustomXValueFormatter
import com.example.test.dto.PitchDto
import com.example.test.dto.TimeDto
import com.example.test.entity.Audio
import com.example.test.entity.Video
import com.example.test.service.AppDataBase
import com.example.test.viewmodel.EmptyVm
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.rczs.gis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mp_charts.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap


class MpChartsActivity : BaseActivity<EmptyVm>()  {
    var audioDateList: ArrayList<Entry> = ArrayList()
    var videoDateList: ArrayList<Entry> = ArrayList()
    var pitchDtoList: ArrayList<PitchDto> = ArrayList()
    var videoList: ArrayList<PitchDto> = ArrayList()
    lateinit var audioAdapter: PitchAdapter
    lateinit var videoAdapter: PitchAdapter
    private var mTimer: Timer? = null
    private var timeTask:TimerTask?=null
    private var i = 0
    private var timeDto:TimeDto?=null
    lateinit var audioMap:LinkedHashMap<String,ArrayList<Audio>>
    lateinit var videoMap:LinkedHashMap<String,ArrayList<Video>>
    var totalMap:LinkedHashMap<String,Int> = LinkedHashMap<String,Int>()
    var totalxMap:LinkedHashMap<Int,String> = LinkedHashMap<Int,String>()

    override fun setResLayout(): Int {
        return R.layout.activity_mp_charts
    }

    override fun initView() {
        ryMessageView.layoutManager = LinearLayoutManager(applicationContext)
        audioAdapter = PitchAdapter()
        ryMessageView.adapter = audioAdapter

        rv_video.layoutManager = LinearLayoutManager(applicationContext)
        videoAdapter = PitchAdapter()
        rv_video.adapter = videoAdapter
    }

    override fun initData() {
        var date = intent.getStringExtra("date")
        var audios = AppDataBase.getDBInstace().getAudioDao().getAllAudios()
        var videos = AppDataBase.getDBInstace().getVideoDao().getAllVideos()

        audioMap = LinkedHashMap<String,ArrayList<Audio>>()
        videoMap = LinkedHashMap<String,ArrayList<Video>>()

        var dateFormatter = SimpleDateFormat("MM/dd/yyyy")
        audios.forEach({
            val date = dateFormatter.format(it.date)
            if(audioMap.containsKey(date)){
                audioMap.get(date)?.add(it)
            }else{
                audioMap[date] = ArrayList<Audio>()
                audioMap[date]?.add(it)
            }
        })

        videos.forEach({
            val date = dateFormatter.format(it.date)
            if(videoMap.containsKey(date)){
                videoMap.get(date)?.add(it)
            }else{
                videoMap[date] = ArrayList<Video>()
                videoMap[date]?.add(it)
            }
        })

        //初始化图表
        initChart(line1)

        //初始化list
        if(date!=null){
            totalMap.get(date)?.let { initRyData(it) }
        }else{
            initRyData(0)
        }

        setupPieChart()
        mTimer = Timer()
        timeTask = object : TimerTask(){
            override fun run() {
                updateUi()
            }
        }
        mTimer?.schedule(timeTask,0,20)
    }

    override fun initOnClick() {
        super.initOnClick()
        line1.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.x?.toInt()?.let { initRyData(it) }
                audioAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected() {

            }

        })

        audioAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                ryMessageView.visibility = View.GONE
                tv_audio_title.visibility = View.GONE
                rv_video.visibility = View.GONE
                tv_video_title.visibility = View.GONE
                timeDto = pitchDtoList.get(position).timeDto
                ll_pie.visibility = View.VISIBLE
                i=0
            }
        })

        videoAdapter.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                ryMessageView.visibility = View.GONE
                tv_audio_title.visibility = View.GONE
                rv_video.visibility = View.GONE
                tv_video_title.visibility = View.GONE
                timeDto = pitchDtoList.get(position).timeDto
                ll_pie.visibility = View.VISIBLE
                i=0
            }
        })

        tv_back.setOnClickListener {
            ryMessageView.visibility = View.VISIBLE
            tv_audio_title.visibility = View.VISIBLE
            rv_video.visibility = View.VISIBLE
            tv_video_title.visibility = View.VISIBLE
            ll_pie.visibility = View.GONE
        }

        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun initChart(chart: LineChart) {

        audioMap.entries.forEach{
            if(!totalMap.containsKey(it.key)){
                totalMap[it.key] = totalMap.keys.size
            }
        }

        videoMap.entries.forEach{
            if(!totalMap.containsKey(it.key)){
                totalMap[it.key] = totalMap.keys.size
            }
        }

        totalMap.entries.forEach{
            totalxMap[it.value] = it.key
        }

        audioMap.entries.forEach{
            audioDateList.add(Entry(totalMap.get(it.key)!!.toFloat(), it.value.size.toFloat()))
        }

        videoMap.entries.forEach{
            videoDateList.add(Entry(totalMap.get(it.key)!!.toFloat(), it.value.size.toFloat()))
        }


        val xVals = ArrayList<String>()
        totalMap.entries.forEach{
            xVals.add(it.key)
        }

        //是否绘制X轴上的网格线（背景里面的竖线）
        chart.xAxis.setDrawGridLines(true)
        //是否绘制Y轴上的网格线（背景里面的横线）
        chart.axisLeft.setDrawGridLines(true)

        val videoData = LineDataSet(videoDateList, "Video")
        val audioData = LineDataSet(audioDateList, "Audio")
        //折线宽度
        videoData.lineWidth = 2f
        videoData.circleRadius = 8f
        videoData.circleHoleRadius = 3f
        videoData.color = Color.BLUE
        videoData.setCircleColor(Color.BLUE)

        audioData.lineWidth = 2f
        audioData.circleRadius = 8f
        audioData.circleHoleRadius = 3f
        audioData.color = Color.GREEN
        audioData.setCircleColor(Color.GREEN)
        val lineData = LineData(videoData,audioData)
        chart.data = lineData

        //   X轴所在位置   默认为上面
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setLabelCount(totalMap.keys.size-1)
        //自定义x轴的点标签
        chart.xAxis.setValueFormatter(CustomXValueFormatter(totalMap.keys.toTypedArray().toMutableList()))

        //隐藏右边的Y轴
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = true

        chart.animateX(1000)//X轴的动画
    }

    private fun setupPieChart() {

    }

    private fun initRyData(x:Int){
        pitchDtoList.clear()
        videoList.clear()

        val date = totalxMap.get(x)

        val random = Random()
        if(audioMap!=null && audioMap.entries.size>0){
            var  list = audioMap.get(date)
            if (list != null) {
                for(audio in list){
                    var time1 = random.nextInt(40)+10
                    var time2 = random.nextInt(30)+10
                    var pitchDto = PitchDto(audio.name!!, Date(audio.date!!),TimeDto(time1,time2,100-time1-time2))
                    pitchDtoList.add(pitchDto)
                }
            }
        }
        audioAdapter.setList(pitchDtoList)

        if(videoMap!=null && videoMap.entries.size>0){
            var  list = videoMap.get(date)
            if (list != null) {
                for(video in list){
                    var time1 = random.nextInt(40)+10
                    var time2 = random.nextInt(30)+10
                    var pitchDto = PitchDto(video.name!!, Date(video.date!!),TimeDto(time1,time2,100-time1-time2))
                    videoList.add(pitchDto)
                }
            }
        }
        videoAdapter.setList(videoList)
    }

    private val doActionHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val msgId: Int = msg.what
            when (msgId) {
                1 -> {
                    if(i<100){
                        if(timeDto==null){
                            return
                        }
                        if(i<= timeDto?.gesture!!){
                            progress1.progress = i
                            tv_gesture.text = "${i}%"
                        }

                        if(i<= timeDto?.speed!!){
                            progress2.progress = i
                            tv_speed.text = "${i}%"
                        }

                        if(i<= timeDto?.volume!!){
                            progress3.progress = i
                            tv_volume.text = "${i}%"
                        }
                        i++
                    }
                }
            }
        }
    }

    private fun updateUi(){
        val message = Message()
        message.what = 1
        doActionHandler.sendMessage(message)
    }
    override fun onDestroy() {
        super.onDestroy()
        mTimer?.cancel()
    }
}