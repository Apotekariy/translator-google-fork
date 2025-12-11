package com.example.translator.data.repository

import android.util.LruCache
import com.example.translator.data.local.model.TranslationCacheDto
import com.example.translator.data.mapper.TranslationMapper
import com.example.translator.domain.model.Translation
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalTranslationRepositoryTest {


    private val mockCache = mockk<LruCache<String, TranslationCacheDto>>(relaxed = true)

    private val mapper = TranslationMapper()

    //Создаем тестируемый репозиторий
    private val repository = LocalTranslationRepositoryImpl(mockCache, mapper)

    @Test
    fun saveTranslation_putsDataIntoCache_withCorrectKey() {
        val translation = Translation(
            sourceText = "Cat",
            translatedText = "Кот",
            sourceLang = "en",
            targetLang = "ru",
            timestamp = 100L
        )
        // формат ключа: sourceLang_targetLang_sourceText
        val expectedKey = "en_ru_Cat"

        val expectedDto = TranslationCacheDto(
            sourceText = "Cat",
            translatedText = "Кот",
            sourceLang = "en",
            targetLang = "ru",
            timestamp = 100L
        )

        runTest {
            repository.saveTranslation(translation)
        }

        verify { mockCache.put(expectedKey, expectedDto) }
    }

    @Test
    fun getTranslation_getsDtoFromCache_andMapsToDomain() = runTest {
        val key = "en_ru_Dog"
        val cachedDto = TranslationCacheDto(
            sourceText = "Dog",
            translatedText = "Собака",
            sourceLang = "en",
            targetLang = "ru",
            timestamp = 200L
        )

        every { mockCache.get(key) } returns cachedDto

        val result = repository.getTranslation("Dog", "en", "ru")

        // Проверяем, что вернулась доменная модель с правильными данными
        assertEquals("Собака", result?.translatedText)
        assertEquals(200L, result?.timestamp)

        verify { mockCache.get(key) }
    }
}