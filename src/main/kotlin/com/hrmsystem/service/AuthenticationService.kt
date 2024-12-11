package com.hrmsystem.service

import com.hrmsystem.dto.UserRegistrationDto
import com.hrmsystem.dto.AuthenticationResponse
import com.hrmsystem.entity.User
import com.hrmsystem.exception.UserAlreadyExistsException
import com.hrmsystem.exception.UserNotFoundException
import com.hrmsystem.exception.InvalidCredentialsException
import com.hrmsystem.repository.UserRepository
import com.hrmsystem.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Transactional
    fun registerUser(registrationDto: UserRegistrationDto): AuthenticationResponse {
        // Validate unique username and email
        if (userRepository.existsByUsername(registrationDto.username)) {
            throw UserAlreadyExistsException("Username already exists")
        }

        if (userRepository.existsByEmail(registrationDto.email)) {
            throw UserAlreadyExistsException("Email already exists")
        }

        // Create and save new user
        val user = User(
            username = registrationDto.username,
            password = passwordEncoder.encode(registrationDto.password),
            email = registrationDto.email,
            lastLogin = LocalDateTime.now()
        )

        val savedUser = userRepository.save(user)

        // Generate JWT token
        val token = jwtTokenProvider.generateToken(
            savedUser.username,
            savedUser.role.name
        )

        return AuthenticationResponse(
            token = token,
            userId = savedUser.id!!,
            username = savedUser.username,
            role = savedUser.role.name
        )
    }

    fun authenticateUser(username: String, password: String): AuthenticationResponse {
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        if (!passwordEncoder.matches(password, user.password)) {
            throw InvalidCredentialsException("Invalid credentials")
        }

        // Update last login
        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)

        // Generate JWT token
        val token = jwtTokenProvider.generateToken(
            user.username,
            user.role.name
        )

        return AuthenticationResponse(
            token = token,
            userId = user.id!!,
            username = user.username,
            role = user.role.name
        )
    }
}