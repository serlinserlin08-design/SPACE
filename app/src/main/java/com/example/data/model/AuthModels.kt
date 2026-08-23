package com.example.data.model

data class AuthUser(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
    val isGuest: Boolean = false,
    val loginTime: Long = System.currentTimeMillis()
)

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val user: AuthUser) : AuthState
    data class Error(val message: String) : AuthState
}
