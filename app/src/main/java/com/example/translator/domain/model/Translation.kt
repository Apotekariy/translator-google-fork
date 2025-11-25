package com.example.translator.domain.model

data class Translation(
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val timestamp: Long
)