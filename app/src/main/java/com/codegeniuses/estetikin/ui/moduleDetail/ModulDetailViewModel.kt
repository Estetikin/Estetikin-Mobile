package com.codegeniuses.estetikin.ui.moduleDetail

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class ModulDetailViewModel(private val repo: Repository) : ViewModel() {
    fun getAllModuleDetail() = repo.getAllModuleDetail()
}
