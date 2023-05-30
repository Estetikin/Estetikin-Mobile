package com.codegeniuses.estetikin.ui.signup

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class SignUpViewModel(private val repo: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String, confirmPassword: String) =
        repo.register(name, email, password, confirmPassword)
}