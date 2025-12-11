package com.example.translator.domain.repository

import com.example.translator.domain.model.Translation

interface ILocalTranslationRepository {
    suspend fun getTranslation(text: String, sourceLang: String, targetLang: String): Translation?
    suspend fun saveTranslation(translation: Translation)
}