package com.app.githubusersmarco.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.githubusersmarco.model.GitHubUser
import com.app.githubusersmarco.model.UserRepository
import com.app.githubusersmarco.service.GitHubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitHubRepository(private val service: GitHubService, activity: Activity) {
    private val _usersLiveData = MutableLiveData<List<GitHubUser>>()
    val usersLiveData: LiveData<List<GitHubUser>> = _usersLiveData
    private val _repositoriesLiveData = MutableLiveData<List<UserRepository>>()
    val repositoriesLiveData: LiveData<List<UserRepository>> = _repositoriesLiveData

    suspend fun getUsers() {
        try {
            val response = withContext(Dispatchers.IO) {
                service.getUsers().execute()
            }

            if (response.isSuccessful) {
                val users = response.body()
                _usersLiveData.postValue(users)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
