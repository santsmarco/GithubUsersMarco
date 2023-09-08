package com.app.githubusersmarco.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.githubusersmarco.repository.GitHubRepository
import com.app.githubusersmarco.viewModel.GitHubViewModel

class GitHubViewModelFactory(private val repository: GitHubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GitHubViewModel::class.java)) {
            return GitHubViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}