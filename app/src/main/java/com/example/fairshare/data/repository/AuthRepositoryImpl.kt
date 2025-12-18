package com.example.fairshare.data.repository

import com.example.fairshare.data.remote.AuthApi
import com.example.fairshare.data.remote.NetworkModule
import com.example.fairshare.data.remote.models.AuthRequest
import com.example.fairshare.domain.repository.AuthRepository
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Default implementation of [AuthRepository] using Retrofit [AuthApi].
 *
 * Handles network communication for authentication operations and provides
 * user friendly error messages for common failure scenarios.
 *
 * @property api Retrofit API interface for authentication endpoints.
 *               Defaults to [NetworkModule.authApi].
 * @property gson Gson instance used to parse API error response bodies.
 */
class AuthRepositoryImpl(
    private val api: AuthApi = NetworkModule.authApi,
    private val gson: Gson = Gson(),
) : AuthRepository {
    /**
     * Authenticates a user with their email and password.
     *
     * On successful authentication, the server session cookie is automatically
     * stored by the network module's cookie jar for subsequent authenticated requests.
     *
     * @param email User's email address.
     * @param password User's password.
     * @return [Result.success] containing a server message on successful login,
     *         or [Result.failure] with a user friendly error message on failure.
     */
    override suspend fun login(
        email: String,
        password: String,
    ): Result<String> = call { api.login(AuthRequest(email, password)) }

    /**
     * Registers a new user account with the provided credentials.
     *
     * @param email User's email address (must be unique in the system).
     * @param password User's chosen password.
     * @return [Result.success] containing a server message on successful registration,
     *         or [Result.failure] with a user friendly error message on failure.
     */
    override suspend fun register(
        email: String,
        password: String,
    ): Result<String> = call { api.register(AuthRequest(email, password)) }

    /**
     * Executes a Retrofit call and wraps the result in a [Result] with error handling.
     *
     * Normalizes various error types into user friendly messages:
     *
     * @param block Suspending lambda that executes the Retrofit API call.
     * @return [Result.success] with response body on success,
     *         or [Result.failure] with an exception containing a user-friendly message.
     */
    private suspend fun call(block: suspend () -> Response<String>): Result<String> =
        try {
            val resp = block()
            if (resp.isSuccessful) {
                Result.success(resp.body().orEmpty())
            } else {
                Result.failure(Exception(humanize(resp)))
            }
        } catch (e: IOException) {
            Result.failure(Exception("No internet connection or timeout. Please try again."))
        } catch (e: HttpException) {
            Result.failure(Exception("Server unreachable. Please try again later."))
        } catch (e: Exception) {
            Result.failure(Exception("Something went wrong. Please try again."))
        }

    /**
     * Converts a failed HTTP response into a human-readable error message.
     *
     * Attempts to parse the error body as JSON to extract server-provided messages.
     * Falls back to status code based messages when parsing fails.
     *
     * @param resp The failed HTTP response to convert.
     * @return A user friendly error message describing the failure.
     */
    private fun humanize(resp: Response<*>): String {
        if (resp.code() == 401) return "Incorrect email or password"

        val raw = resp.errorBody()?.string().orEmpty()
        val msg =
            try {
                gson.fromJson(raw, ApiError::class.java)?.message
            } catch (_: Exception) {
                null
            }
        return msg?.takeIf { it.isNotBlank() }
            ?: when (resp.code()) {
                400 -> "Invalid request"
                403 -> "You don’t have permission to do that"
                404 -> "Not found"
                500, 502, 503 -> "Server error. Please try again later"
                else -> "Unexpected error"
            }
    }

    /**
     * Internal model for parsing server error response JSON.
     *
     * @property message Optional error message from the server.
     */
    private data class ApiError(
        val message: String?,
    )
}
