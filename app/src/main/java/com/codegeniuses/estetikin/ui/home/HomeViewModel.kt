package com.codegeniuses.estetikin.ui.home

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class HomeViewModel(private val repo: Repository) : ViewModel(){

    fun getNickname(){
        repo.getNickname()
    }
}