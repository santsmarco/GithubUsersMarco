package com.app.githubusersmarco.model

data class UserRepository(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    val stargazersCount: Int,
    val forksCount: Int
)