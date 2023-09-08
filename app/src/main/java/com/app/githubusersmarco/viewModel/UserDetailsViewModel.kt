package com.app.githubusersmarco.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.githubusersmarco.repository.UserDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDetailsViewModel(private val repository: UserDetailsRepository) : ViewModel() {
    val userDetailLiveData = repository.userDetailLiveData
    val repositoriesLiveData = repository.repositoriesLiveData

    fun getDetailUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchUserDetails()
        }
    }

    fun getUserRepositories(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchUserRepositories(username)
        }
    }
}