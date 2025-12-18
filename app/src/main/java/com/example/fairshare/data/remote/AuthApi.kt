package com.example.fairshare.data.remote

import com.example.fairshare.data.remote.models.AuthRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * API interface for authentication endpoints.
 *
 * Provides methods to register new users and authenticate existing users.
 * All endpoints are public (no authentication required).
 */
interface AuthApi {
    /**
     * Registers a new user account.
     *
     * @param body Authentication credentials containing email and password.
     * @return [Response] containing a success message string on successful registration,
     *         or an error response with appropriate HTTP status code.
     */
    @POST("public/register")
    @Headers("Content-Type: application/json")
    suspend fun register(
        @Body body: AuthRequest,
    ): Response<String>

    /**
     * Authenticates an existing user.
     *
     * On successful authentication, the server sets a session cookie that will be
     * automatically managed by the [NetworkModule]'s cookie jar for subsequent requests.
     *
     * @param body Authentication credentials containing email and password.
     * @return [Response] containing a success message string on successful login,
     *         or an error response (e.g., 401 for invalid credentials).
     */
    @POST("public/login")
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body body: AuthRequest,
    ): Response<String>
}
