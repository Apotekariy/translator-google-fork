package com.example.translator.domain.usecase

import com.example.translator.domain.model.Result
import com.example.translator.domain.model.Translation
import com.example.translator.domain.repository.TranslationRepository
import javax.inject.Inject

class TranslateTextUseCase @Inject constructor(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke(
        text: String,
        sourceLang: String,
        targetLang: String
    ): Result<Translation> {
        return try {
            val translation = repository.translate(text, sourceLang, targetLang)
            Result.Success(translation)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}