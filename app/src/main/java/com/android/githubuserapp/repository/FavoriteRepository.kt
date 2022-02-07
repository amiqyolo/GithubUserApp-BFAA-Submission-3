package com.android.githubuserapp.repository

import androidx.lifecycle.LiveData
import com.android.githubuserapp.data.local.FavoriteDao
import com.android.githubuserapp.data.model.FavoriteUser

class FavoriteRepository(private val mRoomDao: FavoriteDao) {
    fun getAllFavorites(): LiveData<List<FavoriteUser>> = mRoomDao.getAllFavorites()
}