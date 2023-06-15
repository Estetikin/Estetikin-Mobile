package com.codegeniuses.estetikin.ui.forgetPassword


import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class ForgetPasswordViewModel(val repo: Repository) : ViewModel() {

    fun sendEmail(email: String) = repo.sendEmail(email)
}