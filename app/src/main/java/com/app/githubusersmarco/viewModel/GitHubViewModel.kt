package com.app.githubusersmarco.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.githubusersmarco.repository.GitHubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubViewModel(private val repository: GitHubRepository) : ViewModel() {
    val usersLiveData = repository.usersLiveData

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsers()
        }
    }
}