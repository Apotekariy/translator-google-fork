package com.example.translator.data.mapper

import com.example.translator.data.local.model.TranslationCacheDto
import com.example.translator.domain.model.Translation
import org.junit.Assert.assertEquals
import org.junit.Test

class TranslationMapperTest {

    private val mapper = TranslationMapper()

    @Test
    fun mapToDomain_mapsDtoToTranslationCorrectly() {
        val dto = TranslationCacheDto(
            sourceText = "Hello",
            translatedText = "Привет",
            sourceLang = "en",
            targetLang = "ru",
            timestamp = 12345L
        )

        val domainModel = mapper.mapToDomain(dto)

        assertEquals(dto.sourceText, domainModel.sourceText)
        assertEquals(dto.translatedText, domainModel.translatedText)
        assertEquals(dto.sourceLang, domainModel.sourceLang)
        assertEquals(dto.targetLang, domainModel.targetLang)
        assertEquals(dto.timestamp, domainModel.timestamp)
    }

    @Test
    fun mapToDto_mapsTranslationToDtoCorrectly() {
        val domain = Translation(
            id = 100, // id не должен попасть в Dto
            sourceText = "World",
            translatedText = "Мир",
            sourceLang = "en",
            targetLang = "ru",
            timestamp = 67890L
        )

        val dto = mapper.mapToDto(domain)

        assertEquals(domain.sourceText, dto.sourceText)
        assertEquals(domain.translatedText, dto.translatedText)
        assertEquals(domain.timestamp, dto.timestamp)
    }
}