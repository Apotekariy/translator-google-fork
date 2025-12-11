package com.example.translator.domain.usecase

import com.example.translator.domain.model.Translation
import com.example.translator.domain.repository.ILocalTranslationRepository
import com.example.translator.domain.repository.IRemoteTranslationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTranslationUseCaseTest {

    // relaxed = true, чтобы методы типа saveTranslation (которые ничего не возвращают) не требовали настройки
    private val localRepo = mockk<ILocalTranslationRepository>(relaxed = true)
    private val remoteRepo = mockk<IRemoteTranslationRepository>()

    private val useCase = GetTranslationUseCase(localRepo, remoteRepo)

    @Test
    fun invoke_returnsCachedData_whenAvailable_andDoesNotCallRemote() = runTest {
        val text = "Hello"
        val cachedTranslation = Translation(
            sourceText = text, translatedText = "Привет",
            sourceLang = "en", targetLang = "ru", timestamp = 100L
        )

        // если спросят в локальном репо - верни этот объект
        coEvery { localRepo.getTranslation(text, "en", "ru") } returns cachedTranslation

        val result = useCase(text, "en", "ru")

        assertEquals("Привет", result.translatedText)

        // Проверяем, что сеть НЕ вызывалась
        coVerify(exactly = 0) { remoteRepo.fetchTranslation(any(), any(), any()) }
    }

    @Test
    fun invoke_callsRemote_andSavesToLocal_whenCacheIsEmpty() = runTest {
        val text = "World"

        // Настраиваем: в кэше пусто
        coEvery { localRepo.getTranslation(text, "en", "ru") } returns null
        // Настраиваем: сеть вернет "Мир"
        coEvery { remoteRepo.fetchTranslation(text, "en", "ru") } returns "Мир"

        val result = useCase(text, "en", "ru")

        assertEquals("Мир", result.translatedText)

        // Проверяем вызов сети
        coVerify(exactly = 1) { remoteRepo.fetchTranslation(text, "en", "ru") }
        // Проверяем сохранение в кэш
        coVerify(exactly = 1) { localRepo.saveTranslation(any()) }
    }
}