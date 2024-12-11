package com.hrmsystem.controller

import com.hrmsystem.dto.UserRegistrationDto
import com.hrmsystem.dto.AuthenticationResponse
import com.hrmsystem.service.AuthenticationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/register")
    fun registerUser(
        @Valid @RequestBody registrationDto: UserRegistrationDto
    ): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.registerUser(registrationDto)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.authenticateUser(
            loginRequest.username,
            loginRequest.password
        )
        return ResponseEntity.ok(response)
    }
}

// DTO for login request
data class LoginRequest(
    val username: String,
    val password: String
)