package com.codegeniuses.estetikin.ui.modul

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.model.response.module.DataItem

class ModuleAdapter : RecyclerView.Adapter<ModuleAdapter.ListViewHolder>() {
    private var currentList: List<DataItem> = emptyList()

    private lateinit var onItemClickCallback: OnItemClickCallBack


    interface OnItemClickCallBack {
        fun onItemClicked(data: DataItem)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvModuleTitle: TextView = itemView.findViewById(R.id.tv_item_module_title)
        val imgUrl: ImageView = itemView.findViewById(R.id.iv_icon_item_module)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val title = currentList[position].title

        Glide.with(holder.itemView.context)
            .load(currentList[position].url)
            .into(holder.imgUrl)

        holder.tvModuleTitle.text = title

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentList[holder.adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = currentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setModuleData(articles: List<DataItem>) {
        currentList = articles
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallback
    }
}