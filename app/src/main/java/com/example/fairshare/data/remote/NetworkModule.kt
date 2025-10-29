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

object NetworkModule {
    private const val BASE_URL = "https://ojasaar.com/fairshareapi/"

    // --- Simple in-memory CookieJar (works until app is closed) ---
    private val cookieJar = object : CookieJar {
        private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            val host = url.host
            val existingCookies = cookieStore[host] ?: mutableListOf()

            // Replace cookies with same name
            val newCookies = existingCookies.filter { existing ->
                cookies.none { it.name == existing.name }
            } + cookies

            cookieStore[host] = newCookies.toMutableList()
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val cookies = cookieStore[url.host] ?: return emptyList()

            val now = System.currentTimeMillis()
            // Filter out expired cookies
            return cookies.filter { it.expiresAt > now }
        }
    }

    private val logging = HttpLoggingInterceptor().apply {
        // Use Level.BASIC in production
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthApi::class.java)
    }

    val groupsApi: GroupsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // same OkHttpClient (has cookieJar!)
            .build()
            .create(GroupsApi::class.java)
    }
}