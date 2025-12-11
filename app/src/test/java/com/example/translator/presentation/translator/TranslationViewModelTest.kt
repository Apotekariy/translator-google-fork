package com.example.translator.presentation.translator

import com.example.translator.MainDispatcherRule
import com.example.translator.domain.model.Translation
import com.example.translator.domain.usecase.GetTranslationUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class TranslationViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCase = mockk<GetTranslationUseCase>()

    private lateinit var viewModel: TranslationViewModel

    @Test
    fun translate_updatesStateToResult_onSuccess() = runTest {
        val translation = Translation(sourceText = "Dog", translatedText = "Собака", sourceLang = "en", targetLang = "ru", timestamp = 1L)
        coEvery { useCase("Dog", "en", "ru") } returns translation
        viewModel = TranslationViewModel(useCase)
        viewModel.translate("Dog", "en", "ru")
        val currentState = viewModel.state.value
        assertTrue("State should be Result", currentState is TranslationState.Result)
        assertEquals("Собака", (currentState as TranslationState.Result).translation.translatedText)
    }

    @Test
    fun translate_updatesStateToError_onException() = runTest {
        // Подготавливаем ошибку (например нет интернета)
        val errorMessage = "Network Connection Error"
        coEvery { useCase(any(), any(), any()) } throws RuntimeException(errorMessage)

        viewModel = TranslationViewModel(useCase)

        viewModel.translate("Cat", "en", "ru")

        val currentState = viewModel.state.value

        assertTrue("State should be Error", currentState is TranslationState.Error)
        assertEquals(errorMessage, (currentState as TranslationState.Error).message)
    }
}