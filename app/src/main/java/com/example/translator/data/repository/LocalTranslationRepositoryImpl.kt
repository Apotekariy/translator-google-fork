package com.example.translator.data.repository

import android.util.LruCache
import com.example.translator.data.local.model.TranslationCacheDto
import com.example.translator.data.mapper.TranslationMapper
import com.example.translator.domain.model.Translation
import com.example.translator.domain.repository.ILocalTranslationRepository
import javax.inject.Inject

class LocalTranslationRepositoryImpl @Inject constructor(
    private val cache: LruCache<String, TranslationCacheDto>,
    private val mapper: TranslationMapper
) : ILocalTranslationRepository {

    override suspend fun getTranslation(text: String, sourceLang: String, targetLang: String): Translation? {
        val key = generateKey(text, sourceLang, targetLang)
        val cachedDto = cache.get(key)

        return cachedDto?.let { mapper.mapToDomain(it) }
    }

    override suspend fun saveTranslation(translation: Translation) {
        val key = generateKey(translation.sourceText, translation.sourceLang, translation.targetLang)

        // Превращаем DomainModel в Dto перед сохранением
        val dto = mapper.mapToDto(translation)
        cache.put(key, dto)
    }

    private fun generateKey(text: String, sourceLang: String, targetLang: String): String {
        return "${sourceLang}_${targetLang}_${text}"
    }
}