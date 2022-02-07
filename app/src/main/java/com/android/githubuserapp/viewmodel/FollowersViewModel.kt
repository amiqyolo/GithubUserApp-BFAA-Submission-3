package com.android.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.repository.FollowersRepository
import com.android.githubuserapp.utility.AppResult
import com.android.githubuserapp.utility.MainState
import kotlinx.coroutines.launch

class FollowersViewModel(private val repository: FollowersRepository) : ViewModel() {

    private val _listFollowersData = MutableLiveData<ArrayList<User>?>()
    val listFollowersData: LiveData<ArrayList<User>?> = _listFollowersData

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private fun stateLoading(isLoading: Boolean) {
        _state.value = MainState.Loading(isLoading)
    }

    private fun stateServer(isError: Boolean, msg: String?) {
        _state.value = MainState.ServerState(isError, msg)
    }

    fun setFollowersData(login: String) {
        stateLoading(true)
        viewModelScope.launch {
            val response = repository.getFollowersUser(login)
            stateLoading(false)
            when (response) {
                is AppResult.Success -> {
                    val result = response.data
                    stateServer(false, null)
                    _listFollowersData.value = result as ArrayList<User>
                }
                is AppResult.Error -> {
                    stateServer(true, response.message.toString())
                }
            }
        }
    }
}