package com.example.fairshare.domain.repository

import com.example.fairshare.data.remote.AuthRequest
import com.example.fairshare.data.remote.NetworkModule
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AuthRepository(
    private val api: com.example.fairshare.data.remote.AuthApi = NetworkModule.authApi,
    private val gson: Gson = Gson()
) {

    suspend fun login(email: String, password: String): Result<String> =
        call { api.login(AuthRequest(email, password)) }

    suspend fun register(email: String, password: String): Result<String> =
        call { api.register(AuthRequest(email, password)) }


    private suspend fun call(block: suspend () -> Response<String>): Result<String> =
        try {
            val resp = block()
            if (resp.isSuccessful) {
                Result.success(resp.body().orEmpty())
            } else {
                Result.failure(Exception(humanize(resp)))
            }
        } catch (e: IOException) {
            // No internet / timeout / DNS
            Result.failure(Exception("No internet connection or timeout. Please try again."))
        } catch (e: HttpException) {
            Result.failure(Exception("Server unreachable. Please try again later."))
        } catch (e: Exception) {
            Result.failure(Exception("Something went wrong. Please try again."))
        }

    private fun humanize(resp: Response<*>): String {
        // 401 — wrong creds
        if (resp.code() == 401) return "Incorrect email or password"

        // Try to use server message if present: {"message":"..."}
        val raw = resp.errorBody()?.string().orEmpty()
        val msg = try { gson.fromJson(raw, ApiError::class.java)?.message } catch (_: Exception) { null }
        return msg?.takeIf { it.isNotBlank() }
            ?: when (resp.code()) {
                400 -> "Invalid request"
                403 -> "You don’t have permission to do that"
                404 -> "Not found"
                500, 502, 503 -> "Server error. Please try again later"
                else -> "Unexpected error"
            }
    }

    private data class ApiError(val message: String?)
}