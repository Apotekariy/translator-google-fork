package com.example.translator.data.local.model

data class TranslationCacheDto(
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val timestamp: Long
)