package com.app.githubusersmarco.repository

import androidx.lifecycle.MutableLiveData
import com.app.githubusersmarco.model.UserDetails
import com.app.githubusersmarco.model.UserRepository
import com.app.githubusersmarco.service.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsRepository(private val gitHubService: GitHubService, private val username: String) {

    val userDetailLiveData: MutableLiveData<UserDetails> = MutableLiveData()
    val repositoriesLiveData: MutableLiveData<List<UserRepository>> = MutableLiveData()

    fun fetchUserDetails() {
        gitHubService.getUserDetails(username).enqueue(object : Callback<UserDetails> {
            override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                if (response.isSuccessful) {
                    val userDetails = response.body()
                    userDetails?.let {
                        userDetailLiveData.postValue(it)
                    }
                }
            }

            override fun onFailure(call: Call<UserDetails>, t: Throwable) {
            }
        })
    }

    fun fetchUserRepositories(username: String) {
        gitHubService.getUserRepositories(this.username).enqueue(object : Callback<List<UserRepository>> {
            override fun onResponse(call: Call<List<UserRepository>>, response: Response<List<UserRepository>>) {
                if (response.isSuccessful) {
                    val repositories = response.body()
                    repositories?.let {
                        repositoriesLiveData.postValue(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<UserRepository>>, t: Throwable) {
            }
        })
    }
}