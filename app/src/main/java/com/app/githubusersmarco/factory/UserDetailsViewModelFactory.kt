package com.app.githubusersmarco.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.githubusersmarco.repository.UserDetailsRepository
import com.app.githubusersmarco.viewModel.UserDetailsViewModel

class UserDetailsViewModelFactory(private val repository: UserDetailsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
            return UserDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}