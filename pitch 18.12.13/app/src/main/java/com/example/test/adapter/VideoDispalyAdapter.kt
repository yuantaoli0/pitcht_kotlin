package com.example.test.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.test.R
import com.example.test.dto.VideoDisplayDto

class VideoDispalyAdapter(activity: Context, itemList: ArrayList<VideoDisplayDto>) : BaseAdapter(){
    private var context: Context? = null
    private var itemList: ArrayList<VideoDisplayDto>? = null

    //初始化函数。类似于java中的构造器执行顺序
    init {
        this.context = activity
        this.itemList = itemList
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: TestViewHolder
        var view:View

        if(convertView==null){
            view = View.inflate(context, R.layout.item_display_video,null)
            holder = TestViewHolder(view)
            view.tag = holder
        }else {
            view = convertView
            holder = view.tag as TestViewHolder
        }
        holder.video.setImageDrawable(itemList?.get(position)?.pidId?.let { context?.getDrawable(it) })
        return view
    }

    override fun getItem(position: Int): Any {
        return itemList?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList?.size!!
    }


    class TestViewHolder(var view: View){
        var video:ImageView = view.findViewById(R.id.iv_video)
    }
}