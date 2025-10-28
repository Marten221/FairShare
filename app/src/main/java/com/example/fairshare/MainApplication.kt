package com.example.fairshare

import android.app.Application
import androidx.room.Room
import com.example.fairshare.data.local.GroupDatabase
import com.example.fairshare.data.repository.RoomGroupsRepository
import com.example.fairshare.domain.repository.GroupsRepository

class MainApplication: Application() {

    companion object {
        lateinit var groupDatabase: GroupDatabase
        lateinit var groupsRepository: GroupsRepository
    }

    override fun onCreate() {
        super.onCreate()
        groupDatabase = Room.databaseBuilder(
            applicationContext,
            GroupDatabase::class.java,
            GroupDatabase.NAME
        ).build()

        groupsRepository = RoomGroupsRepository(groupDatabase)
    }
}