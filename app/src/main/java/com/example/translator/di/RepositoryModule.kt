package com.example.translator.di

import com.example.translator.data.local.TranslationDao
import com.example.translator.data.remote.TranslationApi
import com.example.translator.data.repository.TranslationRepositoryImpl
import com.example.translator.domain.repository.TranslationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTranslationRepository(
        api: TranslationApi,
        dao: TranslationDao
    ): TranslationRepository {
        return TranslationRepositoryImpl(api, dao)
    }
}