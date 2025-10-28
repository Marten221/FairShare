package com.example.fairshare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fairshare.data.local.entity.GroupEntity

@Database(entities = [GroupEntity::class], version = 1)
abstract class GroupDatabase: RoomDatabase() {

    companion object{
        const val NAME = "Group_DB"
    }

    abstract fun getGroupDAO(): GroupDAO
}