package com.codegeniuses.estetikin.ui.album

import androidx.lifecycle.ViewModel
import com.codegeniuses.estetikin.data.repository.Repository

class AlbumViewModel(private val repo: Repository) : ViewModel() {
    fun getHistoryAlbum() = repo.getHisotryAlbum()

}
