package com.android.githubuserapp.repository

import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.helper.MappingHelper.responseFollowersFollowingToUser
import com.android.githubuserapp.networking.ApiService
import com.android.githubuserapp.utility.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FollowersRepository(private val apiService: ApiService) {

    suspend fun getFollowersUser(login: String): AppResult<List<User>> {
        return try {
            val response = apiService.getFollowersUser(login)
            if (response.isSuccessful) {
                response.body().let {
                    withContext(Dispatchers.IO) {
                        val result = responseFollowersFollowingToUser(it!!)
                        AppResult.Success(result)
                    }
                }
            } else {
                AppResult.Error(response.code().toString())
            }
        } catch (e: Exception) {
            AppResult.Error(e.message.toString())
        }
    }

}