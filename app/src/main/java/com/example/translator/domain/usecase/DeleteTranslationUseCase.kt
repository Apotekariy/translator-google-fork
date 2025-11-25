package com.example.translator.domain.usecase

import com.example.translator.domain.model.Translation
import com.example.translator.domain.repository.TranslationRepository
import javax.inject.Inject

class DeleteTranslationUseCase @Inject constructor(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke(translation: Translation) {
        repository.deleteTranslation(translation)
    }
}