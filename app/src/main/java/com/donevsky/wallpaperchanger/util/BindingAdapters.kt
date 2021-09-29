package com.donevsky.wallpaperchanger.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.donevsky.wallpaperchanger.R
import com.donevsky.wallpaperchanger.network.Wallpaper
import com.donevsky.wallpaperchanger.wallpaperoverview.WallpaperRecyclerViewAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Wallpaper>?){
    val adapter = recyclerView.adapter as WallpaperRecyclerViewAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, wallpaperUrl: String?){
    wallpaperUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_baseline_broken_image_24))
            .into(imageView)
    }
}