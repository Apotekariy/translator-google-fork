package com.example.translator.data.repository

import com.example.translator.data.local.TranslationDao
import com.example.translator.data.local.TranslationEntity
import com.example.translator.data.remote.TranslationApi
import com.example.translator.domain.model.Translation
import com.example.translator.domain.repository.TranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val api: TranslationApi,
    private val dao: TranslationDao
) : TranslationRepository {

    override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): Translation {
        val cached = dao.getTranslation(text, sourceLang, targetLang)
        if (cached != null) {
            return cached.toDomain()
        }

        val result = api.translate(
            sourceLang = sourceLang,
            targetLang = targetLang,
            text = text
        )

        val translatedText = result.firstOrNull() ?: throw Exception("Empty response")

        val entity = TranslationEntity(
            sourceText = text,
            translatedText = translatedText,
            sourceLang = sourceLang,
            targetLang = targetLang,
            timestamp = System.currentTimeMillis()
        )

        dao.insertTranslation(entity)

        return entity.toDomain()
    }

    override fun getHistory(): Flow<List<Translation>> {
        return dao.getAllTranslations().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteTranslation(translation: Translation) {
        dao.deleteTranslation(translation.toEntity())
    }

    override suspend fun clearHistory() {
        dao.clearAll()
    }
}

private fun TranslationEntity.toDomain() = Translation(
    id = id,
    sourceText = sourceText,
    translatedText = translatedText,
    sourceLang = sourceLang,
    targetLang = targetLang,
    timestamp = timestamp
)

private fun Translation.toEntity() = TranslationEntity(
    id = id,
    sourceText = sourceText,
    translatedText = translatedText,
    sourceLang = sourceLang,
    targetLang = targetLang,
    timestamp = timestamp
)