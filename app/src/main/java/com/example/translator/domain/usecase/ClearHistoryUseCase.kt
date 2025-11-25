package com.example.translator.domain.usecase

import com.example.translator.domain.repository.TranslationRepository
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke() {
        repository.clearHistory()
    }
}