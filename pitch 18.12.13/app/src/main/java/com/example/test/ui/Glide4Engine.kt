package com.example.test.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.zhihu.matisse.engine.ImageEngine

class Glide4Engine : ImageEngine {
    override fun loadThumbnail(
        context: Context?,
        resize: Int,
        placeholder: Drawable?,
        imageView: ImageView?,
        uri: Uri?
    ) {
        if (imageView != null) {
            if (context != null) {
                Glide.with(context)
                    .asBitmap() // some .jpeg files are actually gif
                    .load(uri)
                    .apply(
                        RequestOptions()
                            .override(resize, resize)
                            .placeholder(placeholder)
                            .centerCrop()
                    )
                    .into(imageView)
            }
        }
    }

    override fun loadGifThumbnail(
        context: Context?,
        resize: Int,
        placeholder: Drawable?,
        imageView: ImageView?,
        uri: Uri?
    ) {
        if (context != null) {
            if (imageView != null) {
                Glide.with(context)
                    .asBitmap() // some .jpeg files are actually gif
                    .load(uri)
                    .apply(
                        RequestOptions()
                            .override(resize, resize)
                            .placeholder(placeholder)
                            .centerCrop()
                    )
                    .into(imageView)
            }
        }
    }

    override fun loadImage(
        context: Context?,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView?,
        uri: Uri?
    ) {
        if (context != null) {
            if (imageView != null) {
                Glide.with(context)
                    .load(uri)
                    .apply(
                        RequestOptions()
                            .override(resizeX, resizeY)
                            .priority(Priority.HIGH)
                            .fitCenter()
                    )
                    .into(imageView)
            }
        }
    }

    override fun loadGifImage(
        context: Context?,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView?,
        uri: Uri?
    ) {
        if (context != null) {
            if (imageView != null) {
                Glide.with(context)
                    .asGif()
                    .load(uri)
                    .apply(
                        RequestOptions()
                            .override(resizeX, resizeY)
                            .priority(Priority.HIGH)
                            .fitCenter()
                    )
                    .into(imageView)
            }
        }
    }

    override fun supportAnimatedGif(): Boolean {
        return true
    }
}