package com.codegeniuses.estetikin.ui.article

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class ArticleViewModel(private val repo: Repository) : ViewModel() {
    fun getArticles(type: String) = repo.getArticles(type)

}