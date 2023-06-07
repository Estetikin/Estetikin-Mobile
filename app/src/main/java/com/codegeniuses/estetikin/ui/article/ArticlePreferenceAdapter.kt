package com.codegeniuses.estetikin.ui.article

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.model.response.ArticleItem

class ArticlePreferenceAdapter : RecyclerView.Adapter<ArticlePreferenceAdapter.ListViewHolder>() {
    private var currentList: List<ArticleItem> = emptyList()

    private lateinit var onItemClickCallback: OnItemClickCallBack


    interface OnItemClickCallBack {
        fun onItemClicked(data: ArticleItem)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvArticleTitle: TextView = itemView.findViewById(R.id.tv_article_title)
        val tvAuthor: TextView = itemView.findViewById(R.id.tv_article_category)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val title = currentList[position].title
        val author = currentList[position].author
        holder.tvArticleTitle.text = title
        holder.tvAuthor.text = author

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(currentList[holder.adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = currentList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setArticlePreferenceData(articles: List<ArticleItem>) {
        currentList = articles
        notifyDataSetChanged()
    }


    fun setOnItemPreferenceClickCallback(onItemClickCallback: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallback
    }
}