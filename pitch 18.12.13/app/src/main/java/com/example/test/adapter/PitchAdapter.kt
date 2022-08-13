package com.example.test.adapter

import android.content.DialogInterface
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.test.R
import com.example.test.dto.PitchDto
import com.example.test.entity.Video
import com.example.test.service.AppDataBase
import com.example.test.ui.VlcPlayerActivity
import java.text.SimpleDateFormat

class PitchAdapter : BaseQuickAdapter<PitchDto, BaseViewHolder>(R.layout.item_pitch) {
    override fun convert(holder: BaseViewHolder, item: PitchDto) {
        var simpleDateFormat = SimpleDateFormat("MM/dd/yyyy")
        holder.getView<TextView>(R.id.tv_name).text = item.name
        holder.getView<TextView>(R.id.tv_time).text = simpleDateFormat.format(item.time)
    }
}