package com.android.githubuserapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.helper.MappingHelper.responseSearchToUser
import com.android.githubuserapp.networking.ApiService
import com.android.githubuserapp.utility.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(
    private val apiService: ApiService,
    private val pref: SettingRepository
) {

    suspend fun getSearchUser(login: String): AppResult<List<User>> {
        return try {
            val response = apiService.getSearchUser(login)
            if (response.isSuccessful) {
                response.body().let {
                    withContext(Dispatchers.IO) {
                        val result = responseSearchToUser(it!!.items)
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

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        withContext(Dispatchers.IO) {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}