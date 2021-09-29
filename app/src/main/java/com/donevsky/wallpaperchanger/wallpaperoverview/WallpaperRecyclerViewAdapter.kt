package com.donevsky.wallpaperchanger.wallpaperoverview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.donevsky.wallpaperchanger.databinding.WallpaperGridListItemBinding
import com.donevsky.wallpaperchanger.network.Wallpaper

class WallpaperRecyclerViewAdapter :
    ListAdapter<Wallpaper, WallpaperRecyclerViewAdapter.GridWallpaperViewHolder>(DiffCallback()) {
    val selectedPhotos = ArrayList<Wallpaper>()
    var isSelectMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridWallpaperViewHolder {
        return GridWallpaperViewHolder(
            WallpaperGridListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            ), this
        )
    }

    override fun onBindViewHolder(holder: GridWallpaperViewHolder, position: Int) {
        val wallpaper = getItem(position)
//        holder.itemView.setOnLongClickListener {
//            onLongClickListener.onLongClick(wallpaper)
//            isSelectMode = true
//            holder.selectOrDeselectWallpapers()
//            true
//        }
        holder.bind(wallpaper)
    }

//    class OnLongClickListener(val longClickListener: (wallpaper: Wallpaper)->Unit){
//        fun onLongClick(wallpaper: Wallpaper) = longClickListener(wallpaper)
//    }

    class GridWallpaperViewHolder(
        private var binding: WallpaperGridListItemBinding,
        private val wallpaperRecyclerViewAdapter: WallpaperRecyclerViewAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnLongClickListener {
                wallpaperRecyclerViewAdapter.isSelectMode = true
                selectOrDeselectWallpapers()
//                binding.checkBox.visibility = View.VISIBLE
                true
            }

            itemView.setOnClickListener {
                if(wallpaperRecyclerViewAdapter.isSelectMode){
                    selectOrDeselectWallpapers()
                }
            }
        }

        fun selectOrDeselectWallpapers(){
            val wallpaper = wallpaperRecyclerViewAdapter.getItem(adapterPosition)
            if (wallpaper.isSelected){
                wallpaperRecyclerViewAdapter.selectedPhotos.remove(wallpaper)
                binding.imageView.setPadding(0)
            }else {
                wallpaperRecyclerViewAdapter.selectedPhotos.add(wallpaper)
                binding.imageView.setPadding(15)

            }
            wallpaper.isSelected = !wallpaper.isSelected
            if (wallpaperRecyclerViewAdapter.selectedPhotos.size == 0){
                wallpaperRecyclerViewAdapter.isSelectMode = false
            }
        }

        fun bind(wallpaper: Wallpaper) {
            binding.wallpaper = wallpaper
            if (wallpaper.isSelected){
                binding.imageView.setPadding(15)
            } else {
                binding.imageView.setPadding(0)
            }

//            itemView.setOnClickListener {
////                val wallpaper = wallpaperRecyclerViewAdapter.getItem(adapterPosition)
//                Log.i("tag", wallpaper.wallpaperUrls.portrait)
//            }
            binding.executePendingBindings()
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Wallpaper>() {
    override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
        return oldItem.id == newItem.id
    }

}
