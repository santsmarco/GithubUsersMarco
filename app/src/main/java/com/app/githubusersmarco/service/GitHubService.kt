package com.app.githubusersmarco.service

import com.app.githubusersmarco.model.GitHubUser
import com.app.githubusersmarco.model.UserDetails
import com.app.githubusersmarco.model.UserRepository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("users")
    fun getUsers(): Call<List<GitHubUser>>

    @GET("users/{username}")
    fun getUserDetails(@Path("username") username: String): Call<UserDetails>

    @GET("users/{username}/repos")
    fun getUserRepositories(@Path("username") username: String): Call<List<UserRepository>>
}