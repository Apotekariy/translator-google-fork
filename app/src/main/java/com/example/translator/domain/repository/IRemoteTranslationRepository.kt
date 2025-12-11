package com.example.translator.domain.repository

interface IRemoteTranslationRepository {
    suspend fun fetchTranslation(text: String, sourceLang: String, targetLang: String): String
}