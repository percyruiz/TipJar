package com.example.tipjar.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tipjar.database.dao.TipJarDao
import com.example.tipjar.database.entity.Tip

@Database(
    entities = [Tip::class],
    version = 1,
    exportSchema = false
)
abstract class TipDatabase: RoomDatabase() {
    abstract fun tipJarDao(): TipJarDao
}
