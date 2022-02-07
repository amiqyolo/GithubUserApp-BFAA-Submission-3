package com.android.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.githubuserapp.data.model.User
import com.android.githubuserapp.repository.MainRepository
import com.android.githubuserapp.utility.AppResult
import com.android.githubuserapp.utility.MainState
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _listSearchData = MutableLiveData<ArrayList<User>>()
    val listSearchData: LiveData<ArrayList<User>> = _listSearchData

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    private fun stateLoading(isLoading: Boolean) {
        _state.value = MainState.Loading(isLoading)
    }

    private fun stateEmpty(isEmpty: Boolean) {
        _state.value = MainState.Empty(isEmpty)
    }

    private fun stateRecycler(isRecycler: Boolean) {
        _state.value = MainState.Recycler(isRecycler)
    }

    private fun stateServer(isError: Boolean, msg: String?) {
        _state.value = MainState.ServerState(isError, msg)
    }

    fun setSearchUser(login: String) {
        stateLoading(true)
        viewModelScope.launch {
            val response = repository.getSearchUser(login)
            stateLoading(false)
            when (response) {
                is AppResult.Success -> {
                    val result = response.data
                    stateServer(false, null)
                    if (result != null) {
                        stateEmpty(false)
                        stateRecycler(true)
                        _listSearchData.value = result as ArrayList<User>
                    } else {
                        stateRecycler(false)
                        stateEmpty(true)
                    }
                }
                is AppResult.Error -> {
                    stateRecycler(false)
                    stateServer(true, response.message.toString())
                }
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSettings()
    }
}