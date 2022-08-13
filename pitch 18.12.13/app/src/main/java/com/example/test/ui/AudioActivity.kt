package com.example.test.ui

import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.widget.Toast
import com.example.test.R
import com.example.test.adapter.VideoAdapter
import com.example.test.audio.agent.MonoAgent
import com.example.test.audio.record.DefaultRecord
import com.example.test.audio.record.RecordConfig
import com.example.test.audio.record.RecordException
import com.example.test.audio.record.RecordManger
import com.example.test.entity.Audio
import com.example.test.service.AppDataBase
import com.example.test.utils.PcmToWavUtil
import com.example.test.viewmodel.UploadVm
import com.rczs.gis.base.BaseActivity
import kotlinx.android.synthetic.main.activity_audio.*
import kotlinx.android.synthetic.main.activity_video.iv_back
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


class AudioActivity : BaseActivity<UploadVm>() {
    lateinit var adapter: VideoAdapter
    var audioPath = "/sdcard/pitch"
    var executorService: ExecutorService? = null
    var customFuture: Future<*>? = null
    var registId: String? = null
    var recordFileName:String? = null

    override fun setResLayout(): Int {
        return R.layout.activity_audio
    }

    override fun initView() {

    }

    override fun initData() {
        executorService = Executors.newFixedThreadPool(1)
    }

    override fun initOnClick() {
        super.initOnClick()
        iv_back.setOnClickListener {
            onBackPressed()
        }

        iv_audio.setOnClickListener {
            val recordState: RecordManger.RecordState = RecordManger.getInstance().recordState
            if (recordState === RecordManger.RecordState.RECORDING) {
                stopRecord()
            } else {
                startRecord()
            }
        }

        iv_list.setOnClickListener {
            startActivity(Intent(this,AudioListActivity::class.java))
        }
    }

    private fun stopRecord() {
        if(RecordManger.getInstance().recordState == RecordManger.RecordState.RECORDING){
            RecordManger.getInstance().unRegistAudioCustom(registId)
            RecordManger.getInstance().stopRecord()
            refreshButton()
            val name:String = recordFileName?.substring(0, recordFileName!!.lastIndexOf(".")).toString()
            var fileFullPath = audioPath+"/"+ name
            val length = PcmToWavUtil.getInstance().pcmToWav("${fileFullPath}.pcm",
                "${fileFullPath}.wav",true)

            val audio:Audio = Audio(null, name, "${fileFullPath}.wav",length.toInt(),System.currentTimeMillis())
            AppDataBase.getDBInstace().getAudioDao().insert(audio)
            if (customFuture != null) {
                customFuture!!.cancel(true)
            }
        }
    }


    private fun getRecordConfig(): RecordConfig? {
        val sampleRate: Int = 8000
        val channel: Int = AudioFormat.CHANNEL_IN_MONO
        val audioformat = AudioFormat.ENCODING_PCM_16BIT
        val minBufferSize = AudioRecord.getMinBufferSize(
            sampleRate, channel,
            audioformat
        )
        return RecordConfig.Builder()
            .audioSource(MediaRecorder.AudioSource.MIC)
            .sampleRateInHz(sampleRate)
            .channelConfig(channel)
            .audioFormat(audioformat)
            .bufferSizeInBytes(10 * minBufferSize)
            .readBufferSize(30 * sampleRate / 1000 * 10) // 30ms
            .build()
    }

    private fun startRecord() {
        try {
            recordFileName = "${System.currentTimeMillis()}.pcm"
            Toast.makeText(applicationContext, "begin start recording", Toast.LENGTH_SHORT).show()
            var recordConfig = getRecordConfig()
            var audioCustomAgent = MonoAgent(vadView)
            vadView.startShow()

            audioCustomAgent.setFilePath(audioPath)
            audioCustomAgent.setFileName(recordFileName)
            registId = UUID.randomUUID().toString()
            RecordManger.getInstance().registAudioCustom(registId, audioCustomAgent)
            customFuture = executorService?.submit(audioCustomAgent)
            RecordManger.getInstance().startRecord(recordConfig, DefaultRecord())
            refreshButton()
        } catch (e: RecordException) {
            e.printStackTrace()
        }
    }

    private fun refreshButton() {
        when (RecordManger.getInstance().recordState) {
            RecordManger.RecordState.RECORDING ->{
                tv_recording.setText(R.string.stop_recorder)
                iv_audio.setImageDrawable(applicationContext.getDrawable(R.mipmap.audio_icon_pressed))
            }

            RecordManger.RecordState.NONE -> {
                tv_recording.setText(R.string.start_recorder)
                iv_audio.setImageDrawable(applicationContext.getDrawable(R.mipmap.audio_icon))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecord()
    }
}