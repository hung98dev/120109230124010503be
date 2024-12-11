package com.hrmsystem.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format")
    val email: String,

    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.EMPLOYEE,

    @Column(name = "is_active")
    val isActive: Boolean = true,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "last_login")
    var lastLogin: LocalDateTime? = null
)