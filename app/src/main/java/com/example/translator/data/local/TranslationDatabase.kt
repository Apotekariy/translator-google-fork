package com.example.translator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TranslationEntity::class], version = 1, exportSchema = false)
abstract class TranslationDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}