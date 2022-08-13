package com.example.test.adapter

import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.test.R
import com.example.test.audio.MediaManager
import com.example.test.dto.AudioDto
import com.example.test.entity.Audio
import com.example.test.service.AppDataBase
import com.example.test.ui.MpChartsActivity
import java.text.SimpleDateFormat
import java.util.*

class AudioAdapter : BaseQuickAdapter<AudioDto, BaseViewHolder>(R.layout.item_audio) {
    override fun convert(holder: BaseViewHolder, item: AudioDto) {
        holder.getView<TextView>(R.id.tv_name).text = item.name
//        holder.getView<TextView>(R.id.tv_length).text = "video length："+(item.length?.div(1000)).toString()+"S"
        var dateFormatter = SimpleDateFormat("MM/dd/yyyy")
        holder.getView<TextView>(R.id.tv_date).text = dateFormatter.format(Date(item.date))

        holder.itemView.setOnClickListener {
            holder.getView<ImageView>(R.id.iv_player).setImageDrawable(context.getDrawable(R.mipmap.iv_pause))
            MediaManager.playSound(item.path,object : MediaPlayer.OnCompletionListener {
                override fun onCompletion(p0: MediaPlayer?) {
                    holder.getView<ImageView>(R.id.iv_player).setImageDrawable(context.getDrawable(R.mipmap.iv_player))
                }
            })
        }

        if(item.state == true){
            holder.getView<ImageView>(R.id.iv_delete).visibility = View.GONE
            holder.getView<ImageView>(R.id.iv_detail).visibility = View.VISIBLE
        }else{
            holder.getView<ImageView>(R.id.iv_delete).visibility = View.VISIBLE
            holder.getView<ImageView>(R.id.iv_detail).visibility = View.GONE
        }

        holder.getView<ImageView>(R.id.iv_detail).setOnClickListener{
            var intent = Intent(context,MpChartsActivity::class.java)
            intent.putExtra("date",dateFormatter.format(Date(item.date)))
            context.startActivity(intent)
        }

        //change name
        holder.getView<TextView>(R.id.tv_name).setOnClickListener{
            if(item.state==false){
                val dialog = MaterialDialog(context)
                dialog.input("input audio name")
                dialog.getInputField().setText(item.name)
                dialog.title(R.string.change_audio_name)
                dialog.negativeButton(R.string.button_cancel) {
                    dialog.dismiss()
                }
                dialog.positiveButton(R.string.dialog_ok) {
                    if(!dialog.getInputField().text.isEmpty()){
                        val data = AppDataBase.getDBInstace().getAudioDao().getAudio(item.audioId)
                        data.name = dialog.getInputField().text.trim().toString()
                        item.name = data.name!!
                        AppDataBase.getDBInstace().getAudioDao().update(data)
                        notifyDataSetChanged()
                    }
                }
                dialog.show()
            }
        }

        holder.getView<ImageView>(R.id.iv_delete).setOnClickListener{
            val Dialog = android.app.AlertDialog.Builder(context).create()

            Dialog.setTitle("Alert")
            Dialog.setMessage("Are you sure to delete?")
            Dialog.setCancelable(false)//用于屏蔽返回键和点击空白处
            Dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel" )
            {
                dialog, _->dialog.dismiss()
            }

            Dialog.setButton(DialogInterface.BUTTON_POSITIVE,"ok"){
                    _, _->
                    remove(item)
                    AppDataBase.getDBInstace().getAudioDao().delete(
                        AppDataBase.getDBInstace().getAudioDao().getAudio(item.audioId))
                    notifyDataSetChanged()
            }

            Dialog.show()
        }
    }
}