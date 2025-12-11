package com.example.translator.data.repository

import com.example.translator.data.remote.TranslationApi
import com.example.translator.domain.repository.IRemoteTranslationRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class RemoteTranslationRepositoryImpl @Inject constructor(
    private val api: TranslationApi
) : IRemoteTranslationRepository {

    override suspend fun fetchTranslation(text: String, sourceLang: String, targetLang: String): String {
        val delayTime = Random.nextLong(300, 1001)
        delay(delayTime)

        val response = api.translate(
            sourceLang = sourceLang,
            targetLang = targetLang,
            text = text
        )

        return response.firstOrNull()
            ?: throw Exception("Remote translation returned empty result")
    }
}