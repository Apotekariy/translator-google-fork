package com.example.translator.data.mapper

import com.example.translator.data.local.model.TranslationCacheDto
import com.example.translator.domain.model.Translation
import javax.inject.Inject

class TranslationMapper @Inject constructor() {

    fun mapToDomain(dto: TranslationCacheDto): Translation {
        return Translation(
            // id = 0, так как в кэше нет id
            sourceText = dto.sourceText,
            translatedText = dto.translatedText,
            sourceLang = dto.sourceLang,
            targetLang = dto.targetLang,
            timestamp = dto.timestamp
        )
    }

    fun mapToDto(domain: Translation): TranslationCacheDto {
        return TranslationCacheDto(
            sourceText = domain.sourceText,
            translatedText = domain.translatedText,
            sourceLang = domain.sourceLang,
            targetLang = domain.targetLang,
            timestamp = domain.timestamp
        )
    }
}