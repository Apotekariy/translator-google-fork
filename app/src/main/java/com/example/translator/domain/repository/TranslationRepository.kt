package com.example.translator.domain.repository

import com.example.translator.domain.model.Translation
import kotlinx.coroutines.flow.Flow

interface TranslationRepository {
    suspend fun translate(text: String, sourceLang: String, targetLang: String): Translation
    fun getHistory(): Flow<List<Translation>>
    suspend fun deleteTranslation(translation: Translation)
    suspend fun clearHistory()
}