package com.example.translator.di

import android.content.Context
import androidx.room.Room
import com.example.translator.data.local.TranslationDao
import com.example.translator.data.local.TranslationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTranslationDatabase(
        @ApplicationContext context: Context
    ): TranslationDatabase {
        return Room.databaseBuilder(
            context,
            TranslationDatabase::class.java,
            "translation_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTranslationDao(database: TranslationDatabase): TranslationDao {
        return database.translationDao()
    }
}