package com.example.fairshare

import android.app.Application
import androidx.room.Room
import com.example.fairshare.data.local.GroupDatabase

class MainApplication: Application() {

    companion object {
        lateinit var groupDatabase: GroupDatabase
    }

    override fun onCreate() {
        super.onCreate()
        groupDatabase = Room.databaseBuilder(
            applicationContext,
            GroupDatabase::class.java,
            GroupDatabase.NAME
        ).build()
    }
}