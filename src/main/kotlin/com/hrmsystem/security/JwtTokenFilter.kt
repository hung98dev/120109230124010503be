package com.hrmsystem.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component
class JwtTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val token = resolveToken(httpRequest)

        if (token != null) {
            try {
                val claims = jwtTokenProvider.validateToken(token)
                val username = claims.subject
                val role = claims["role"] as String

                val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))
                val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)

                SecurityContextHolder.getContext().authentication = authentication
            } catch (ex: Exception) {
                // Token validation failed
                SecurityContextHolder.clearContext()
            }
        }

        chain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}