package com.example.translator.domain.usecase

import com.example.translator.domain.model.Translation
import com.example.translator.domain.repository.TranslationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: TranslationRepository
) {
    operator fun invoke(): Flow<List<Translation>> {
        return repository.getHistory()
    }
}