package com.hrmsystem.dto

data class AuthenticationResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val role: String
)