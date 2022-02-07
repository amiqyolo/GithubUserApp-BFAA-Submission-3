package com.android.githubuserapp.repository

import android.database.Cursor
import com.android.githubuserapp.data.local.FavoriteDao
import com.android.githubuserapp.data.model.FavoriteUser
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.helper.MappingHelper.responseDetailToUser
import com.android.githubuserapp.networking.ApiService
import com.android.githubuserapp.utility.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailRepository(
    private val apiService: ApiService,
    private val mRoomDao: FavoriteDao,
) {
    suspend fun getDetailUser(login: String): AppResult<User> {
        return try {
            val response = apiService.getDetailUser(login)
            if (response.isSuccessful) {
                response.body().let {
                    withContext(Dispatchers.IO) {
                        val result = responseDetailToUser(it!!)
                        AppResult.Success(result)
                    }
                }
            } else {
                AppResult.Error(response.message())
            }
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }

    fun checkFavoriteUser(login: String): Cursor {
        return mRoomDao.checkFavoriteUser(login)
    }

    fun addToFavorite(favoriteUser: FavoriteUser) {
        mRoomDao.addToFavorite(favoriteUser)
    }

    fun deleteFromFavorite(login: String) {
        mRoomDao.deleteFromFavorite(login)
    }

}