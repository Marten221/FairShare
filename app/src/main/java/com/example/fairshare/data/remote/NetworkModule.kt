package com.example.fairshare.data.remote

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network configuration provider for Retrofit APIs.
 *
 * Manages the shared [OkHttpClient] instance with cookie management, logging,
 * and timeout configurations. Provides lazily-initialized API service instances
 * for all backend endpoints.
 *
 * The cookie jar persists session cookies in memory for the duration of the app process,
 * enabling authenticated requests after login without manual token management.
 */
object NetworkModule {

    /** Base URL for all FairShare API endpoints. */
    private const val BASE_URL = "https://ojasaar.com/fairshareapi/"

    /**
     * In-memory cookie jar implementation for session management.
     *
     * Stores cookies per-host and automatically filters expired cookies on request.
     * Cookies are lost when the app process terminates.
     *
     * Note: For production apps requiring persistent authentication,
     * consider using a persistent cookie storage mechanism.
     */
    private val cookieJar = object : CookieJar {
        private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

        /**
         * Saves cookies received from a server response.
         *
         * Replaces existing cookies with the same name to handle cookie updates.
         *
         * @param url The URL that sent the cookies.
         * @param cookies List of cookies to save.
         */
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            val host = url.host
            val existingCookies = cookieStore[host] ?: mutableListOf()

            // Replace cookies with same name
            val newCookies = existingCookies.filter { existing ->
                cookies.none { it.name == existing.name }
            } + cookies

            cookieStore[host] = newCookies.toMutableList()
        }

        /**
         * Returns cookies to include in an outgoing request.
         *
         * Automatically filters out expired cookies before returning.
         *
         * @param url The URL of the outgoing request.
         * @return List of non-expired cookies for the request's host.
         */
        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val cookies = cookieStore[url.host] ?: return emptyList()

            val now = System.currentTimeMillis()
            return cookies.filter { it.expiresAt > now }
        }
    }

    /** HTTP logging interceptor configured for full request/response body logging. */
    private val logging = HttpLoggingInterceptor().apply {
        // Use Level.BASIC in production to reduce log verbosity
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Shared OkHttpClient instance with cookie management, logging, and timeouts.
     *
     * Configuration:
     * - Cookie jar for session persistence
     * - Request/response body logging
     * - 10 second connection timeout
     * - 15 second read timeout
     */
    private val client = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Lazily-initialized authentication API service.
     *
     * Uses both [ScalarsConverterFactory] (for plain text responses) and
     * [GsonConverterFactory] (for JSON) to handle various response formats.
     */
    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthApi::class.java)
    }

    /**
     * Lazily-initialized groups API service.
     *
     * Shares the same [OkHttpClient] to maintain session cookies across API calls.
     */
    val groupsApi: GroupsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GroupsApi::class.java)
    }

    /**
     * Lazily-initialized expenses API service.
     *
     * Shares the same [OkHttpClient] to maintain session cookies across API calls.
     */
    val expensesApi: ExpenseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ExpenseApi::class.java)
    }

    /**
     * Lazily-initialized balance API service.
     *
     * Shares the same [OkHttpClient] to maintain session cookies across API calls.
     */
    val balanceApi: BalanceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(BalanceApi::class.java)
    }
}