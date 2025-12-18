package com.example.fairshare.data.remote.models

/**
 * Request body for authentication API endpoints (login and register).
 *
 * @property email User's email address used as the account identifier.
 * @property password User's password for authentication.
 */
data class AuthRequest(
    val email: String,
    val password: String
)