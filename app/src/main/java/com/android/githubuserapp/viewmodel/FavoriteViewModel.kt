package com.android.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.githubuserapp.data.model.FavoriteUser
import com.android.githubuserapp.repository.FavoriteRepository

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    fun getAllFavUser(): LiveData<List<FavoriteUser>> =
        repository.getAllFavorites()

}