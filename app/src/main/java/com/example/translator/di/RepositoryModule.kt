package com.example.translator.di

import android.util.LruCache
import com.example.translator.data.local.TranslationDao
import com.example.translator.data.local.model.TranslationCacheDto
import com.example.translator.data.mapper.TranslationMapper
import com.example.translator.data.remote.TranslationApi
import com.example.translator.data.repository.LocalTranslationRepositoryImpl
import com.example.translator.data.repository.RemoteTranslationRepositoryImpl
import com.example.translator.data.repository.TranslationRepositoryImpl
import com.example.translator.domain.repository.ILocalTranslationRepository
import com.example.translator.domain.repository.IRemoteTranslationRepository
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

    @Provides
    @Singleton
    fun provideLruCache(): LruCache<String, TranslationCacheDto> {
        // Кэш на 10 элементов, как в задании
        return LruCache(10)
    }

    @Provides
    @Singleton
    fun provideLocalTranslationRepository(
        cache: LruCache<String, TranslationCacheDto>,
        mapper: TranslationMapper
    ): ILocalTranslationRepository {
        return LocalTranslationRepositoryImpl(cache, mapper)
    }
    @Provides
    @Singleton
    fun provideRemoteTranslationRepository(
        api: TranslationApi
    ): IRemoteTranslationRepository {
        return RemoteTranslationRepositoryImpl(api)
    }
}