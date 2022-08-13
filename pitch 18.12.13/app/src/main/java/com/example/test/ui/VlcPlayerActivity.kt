package com.example.test.ui

import android.net.Uri
import com.example.test.R
import com.example.test.viewmodel.EmptyVm
import com.rczs.gis.base.BaseActivity
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class VlcPlayerActivity : BaseActivity<EmptyVm>()  {
    private var libVlc: LibVLC? = null
    private var mediaPlayer: MediaPlayer? = null
    private var videoLayout: VLCVideoLayout? = null

    override fun setResLayout(): Int {
       return R.layout.activity_vlc_player
    }

    override fun initView() {

    }

    override fun initData() {
        libVlc = LibVLC(this)
        mediaPlayer = MediaPlayer(libVlc)
        videoLayout = findViewById(R.id.videoLayout)
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer!!.attachViews(videoLayout!!, null, false, false)

        val media = Media(libVlc, intent.getStringExtra("url"))
        media.setHWDecoderEnabled(true, false)
        media.addOption(":network-caching=600")

        mediaPlayer!!.media = media
        media.release()
        mediaPlayer!!.play()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer!!.stop()
        mediaPlayer!!.detachViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.release()
        libVlc!!.release()
    }
}