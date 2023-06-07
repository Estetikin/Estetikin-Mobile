package com.codegeniuses.estetikin.ui.album

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.model.response.album.AlbumItem


class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ListViewHolder>() {

    private var currentList: List<AlbumItem> = emptyList()
    private lateinit var onItemClickCallback: OnItemClickCallBack

    interface OnItemClickCallBack {
        fun onItemClicked(data: AlbumItem)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_album_photo)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(currentList[position].photoUrl)
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentList[holder.adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = currentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAlbumData(albums: List<AlbumItem>) {
        currentList = albums
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallback
    }
}