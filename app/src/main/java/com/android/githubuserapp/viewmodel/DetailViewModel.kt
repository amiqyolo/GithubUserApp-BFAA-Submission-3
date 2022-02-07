package com.android.githubuserapp.viewmodel

import androidx.lifecycle.*
import com.android.githubuserapp.data.model.FavoriteUser
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.helper.MappingHelper
import com.android.githubuserapp.repository.DetailRepository
import com.android.githubuserapp.utility.AppResult
import com.android.githubuserapp.utility.MainState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val repository: DetailRepository) : ViewModel() {

    private val _listDetailData = MutableLiveData<User>()
    val listDetailData: LiveData<User> = _listDetailData

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private fun stateLoading(isLoading: Boolean) {
        _state.value = MainState.Loading(isLoading)
    }

    private fun stateFavorite(isFavorite: Boolean) {
        _state.value = MainState.Favorites(isFavorite)
    }

    fun setDetailData(login: String) {
        stateLoading(true)
        viewModelScope.launch {
            val result = repository.getDetailUser(login)
            stateLoading(false)
            when (result) {
                is AppResult.Success -> {
                    _listDetailData.value = result.data!!
                }
                else -> return@launch
            }
        }
    }

    fun checkFavoriteUser(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.checkFavoriteUser(login)
            withContext(Dispatchers.Main) {
                val cursor = MappingHelper.cursorToFavUser(result)
                stateFavorite(cursor.isNotEmpty())
            }
        }
    }

    fun addToFavorite(user: User) {
        val result = FavoriteUser(
            login = user.login,
            avatarUrl = user.avatarUrl
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorite(result)
            withContext(Dispatchers.Main) {
                stateFavorite(true)
            }
        }
    }

    fun deleteFromFavorite(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromFavorite(user.login)
            withContext(Dispatchers.Main) {
                stateFavorite(false)
            }
        }
    }
}