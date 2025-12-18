package com.example.fairshare.domain.repository

/**
 * Repository interface for authentication operations.
 *
 * Defines the contract for user authentication functionality.
 * Implementations handle the actual network communication and session management.
 */
interface AuthRepository {
    /**
     * Authenticates a user with their email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     * @return [Result.success] containing a success message from the server,
     *         or [Result.failure] with an exception containing an error message.
     */
    suspend fun login(
        email: String,
        password: String,
    ): Result<String>

    /**
     * Registers a new user account.
     *
     * @param email User's email address (must be unique).
     * @param password User's chosen password.
     * @return [Result.success] containing a success message from the server,
     *         or [Result.failure] with an exception containing an error message.
     */
    suspend fun register(
        email: String,
        password: String,
    ): Result<String>
}
