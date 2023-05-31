package com.codegeniuses.estetikin.ui.login

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class LoginViewModel(private val repo: Repository) : ViewModel() {

    fun login(email: String, password: String) =
        repo.login(email, password)
}
