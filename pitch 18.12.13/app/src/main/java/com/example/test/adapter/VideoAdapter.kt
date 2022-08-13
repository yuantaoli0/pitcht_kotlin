package com.example.test.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.test.R
import com.example.test.dto.VideoDisplayDto
import com.example.test.dto.VideoDto
import com.example.test.entity.Video
import com.example.test.service.AppDataBase
import com.example.test.ui.MpChartsActivity
import com.example.test.ui.VlcPlayerActivity
import java.text.SimpleDateFormat
import java.util.*

class VideoAdapter : BaseQuickAdapter<VideoDto, BaseViewHolder>(R.layout.item_video) {
    override fun convert(holder: BaseViewHolder, item: VideoDto) {
        holder.getView<TextView>(R.id.tv_name).text = item.name
//        holder.getView<TextView>(R.id.tv_length).text = "video length："+(item.length?.div(1000)).toString()+"S"
        var dateFormatter = SimpleDateFormat("MM/dd/yyyy")
        holder.getView<TextView>(R.id.tv_date).text = dateFormatter.format(Date(item.date))
        if(item.state == true){
            holder.getView<ImageView>(R.id.iv_delete).visibility = View.GONE
            holder.getView<ImageView>(R.id.iv_detail).visibility = View.VISIBLE
        }else{
            holder.getView<ImageView>(R.id.iv_delete).visibility = View.VISIBLE
            holder.getView<ImageView>(R.id.iv_detail).visibility = View.GONE
        }


        holder.itemView.setOnClickListener {
            var intent = Intent(context,VlcPlayerActivity::class.java)
            
            intent.putExtra("url",item.path)
            context.startActivity(intent)
        }

        holder.getView<ImageView>(R.id.iv_detail).setOnClickListener{
            var intent = Intent(context,MpChartsActivity::class.java)
            intent.putExtra("date",dateFormatter.format(Date(item.date)))
            context.startActivity(intent)
        }

        holder.getView<TextView>(R.id.tv_name).setOnClickListener{
            if(item.state==false){
                val dialog = MaterialDialog(context)
                dialog.input("input video name")
                dialog.getInputField().setText(item.name)
                dialog.title(R.string.change_audio_name)
                dialog.negativeButton(R.string.button_cancel) {
                    dialog.dismiss()
                }
                dialog.positiveButton(R.string.dialog_ok) {
                    if(!dialog.getInputField().text.isEmpty()){
                        val data = AppDataBase.getDBInstace().getVideoDao().getVideo(item.videoId)
                        data.name = dialog.getInputField().text.trim().toString()
                        item.name = data.name!!
                        AppDataBase.getDBInstace().getVideoDao().update(data)
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
                    AppDataBase.getDBInstace().getVideoDao().delete(AppDataBase.getDBInstace().getVideoDao().getVideo(item.videoId))
                    notifyDataSetChanged()
            }

            Dialog.show()
        }
    }
}