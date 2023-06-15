package com.codegeniuses.estetikin.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codegeniuses.estetikin.data.repository.Repository
import com.codegeniuses.estetikin.di.Injection
import com.codegeniuses.estetikin.ui.album.AlbumViewModel
import com.codegeniuses.estetikin.ui.article.ArticleViewModel
import com.codegeniuses.estetikin.ui.confirmPage.ConfirmViewModel
import com.codegeniuses.estetikin.ui.forgetPassword.ForgetPasswordViewModel
import com.codegeniuses.estetikin.ui.login.LoginViewModel
import com.codegeniuses.estetikin.ui.modul.ModuleViewModel
import com.codegeniuses.estetikin.ui.setting.SettingViewModel
import com.codegeniuses.estetikin.ui.signup.SignUpViewModel

class ViewModelFactory(private val repo: Repository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repo) as T
            }
            modelClass.isAssignableFrom(ArticleViewModel::class.java) -> {
                ArticleViewModel(repo) as T
            }
            modelClass.isAssignableFrom(AlbumViewModel::class.java) -> {
                AlbumViewModel(repo) as T
            }
            modelClass.isAssignableFrom(ModuleViewModel::class.java) -> {
                ModuleViewModel(repo) as T
            }
            modelClass.isAssignableFrom(ConfirmViewModel::class.java) -> {
                ConfirmViewModel(repo) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repo) as T
            }
            modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java) -> {
                ForgetPasswordViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}
