package com.codegeniuses.estetikin.ui.modul

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class ModuleViewModel(private val repo: Repository) : ViewModel() {

    fun getAllModule() = repo.getAllModule()
}
