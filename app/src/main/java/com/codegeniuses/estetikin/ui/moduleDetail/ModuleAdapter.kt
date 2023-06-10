package com.codegeniuses.estetikin.ui.moduleDetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.model.response.module.ContentItem


class ModuleDetailAdapter : RecyclerView.Adapter<ModuleDetailAdapter.ListViewHolder>() {

    private var currentList: List<ContentItem> = emptyList()

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStepNumber: TextView = itemView.findViewById(R.id.tv_step_number)
        val tvStepTitle: TextView = itemView.findViewById(R.id.tv_step_title_placeholder)
        val ivStep: ImageView = itemView.findViewById(R.id.iv_step_module)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_module_text_description_placeholder)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stepNumber = currentList[position].id
        val stepTitle = currentList[position].title
        val desc = currentList[position].description

        Glide.with(holder.itemView.context)
            .load(currentList[position].url)
            .into(holder.ivStep)

        holder.apply {
            tvStepNumber.text = stepNumber.toString()
            tvStepTitle.text = stepTitle
            tvDesc.text = desc
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_module_detail, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = currentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setModuleDetailData(moduleData: List<ContentItem>) {
        currentList = moduleData
        notifyDataSetChanged()
    }
}

