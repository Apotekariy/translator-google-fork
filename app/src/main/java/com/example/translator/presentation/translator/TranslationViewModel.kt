package com.example.translator.presentation.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.domain.model.Translation
import com.example.translator.domain.usecase.GetTranslationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class TranslationState {
    object Idle : TranslationState()
    object Loading : TranslationState()
    data class Result(val translation: Translation) : TranslationState()
    data class Error(val code: String?, val message: String) : TranslationState()
}

@HiltViewModel
class TranslationViewModel @Inject constructor(
    private val getTranslationUseCase: GetTranslationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TranslationState>(TranslationState.Idle)
    val state: StateFlow<TranslationState> = _state.asStateFlow()

    fun translate(text: String, sourceLang: String, targetLang: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _state.value = TranslationState.Loading

            try {
                // Вызываем UseCase
                val result = getTranslationUseCase(text, sourceLang, targetLang)

                // Успех
                _state.value = TranslationState.Result(result)

            } catch (e: Exception) {
                val errorCode = if (e is HttpException) e.code().toString() else null
                val errorMessage = e.localizedMessage ?: "Unknown error"

                _state.value = TranslationState.Error(errorCode, errorMessage)
            }
        }
    }
}