package com.example.translator.presentation.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.domain.model.Language
import com.example.translator.domain.model.Result
import com.example.translator.domain.usecase.TranslateTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TranslatorState(
    val sourceText: String = "",
    val translatedText: String = "",
    val sourceLang: Language = Language("en", "English"),
    val targetLang: Language = Language("ru", "Russian"),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val translateTextUseCase: TranslateTextUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TranslatorState())
    val state = _state.asStateFlow()

    val availableLanguages = listOf(
        Language("en", "English"),
        Language("ru", "Russian"),
        Language("es", "Spanish"),
        Language("fr", "French"),
        Language("de", "German")
    )

    fun onSourceTextChange(text: String) {
        _state.update { it.copy(sourceText = text, error = null) }
    }

    fun onSourceLangChange(language: Language) {
        _state.update { it.copy(sourceLang = language) }
    }

    fun onTargetLangChange(language: Language) {
        _state.update { it.copy(targetLang = language) }
    }

    fun swapLanguages() {
        _state.update {
            it.copy(
                sourceLang = it.targetLang,
                targetLang = it.sourceLang,
                sourceText = it.translatedText,
                translatedText = it.sourceText
            )
        }
    }

    fun translate() {
        val currentState = _state.value
        if (currentState.sourceText.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = translateTextUseCase(
                text = currentState.sourceText,
                sourceLang = currentState.sourceLang.code,
                targetLang = currentState.targetLang.code
            )) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            translatedText = result.data.translatedText,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                is Result.Loading -> {}
            }
        }
    }
}