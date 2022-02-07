package com.android.githubuserapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.githubuserapp.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: MainRepository) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSettings()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }

}