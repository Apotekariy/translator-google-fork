package com.example.translator.domain.usecase

import com.example.translator.domain.model.Translation
import com.example.translator.domain.repository.ILocalTranslationRepository
import com.example.translator.domain.repository.IRemoteTranslationRepository
import javax.inject.Inject

class GetTranslationUseCase @Inject constructor(
    private val localRepository: ILocalTranslationRepository,
    private val remoteRepository: IRemoteTranslationRepository
) {
    suspend operator fun invoke(text: String, sourceLang: String, targetLang: String): Translation {
        // Сначала запрашиваем локальный репозиторий (LruCache)
        val cachedTranslation = localRepository.getTranslation(text, sourceLang, targetLang)

        // в кэше - возвращаем
        if (cachedTranslation != null) {
            return cachedTranslation
        }

        // в кэше нет - remote
        val translatedString = remoteRepository.fetchTranslation(text, sourceLang, targetLang)

        val newTranslation = Translation(
            sourceText = text,
            translatedText = translatedString,
            sourceLang = sourceLang,
            targetLang = targetLang,
            timestamp = System.currentTimeMillis()
        )

        // Кэшируем
        localRepository.saveTranslation(newTranslation)

        return newTranslation
    }
}