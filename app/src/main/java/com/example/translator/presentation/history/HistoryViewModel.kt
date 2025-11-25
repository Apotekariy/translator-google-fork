package com.example.translator.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.domain.model.Translation
import com.example.translator.domain.usecase.ClearHistoryUseCase
import com.example.translator.domain.usecase.DeleteTranslationUseCase
import com.example.translator.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOrder {
    DATE_DESC,
    DATE_ASC,
    SOURCE_TEXT,    // По алфавиту
    TRANSLATED_TEXT // По алфавиту
}

data class HistoryState(
    val translations: List<Translation> = emptyList(),
    val sortOrder: SortOrder = SortOrder.DATE_DESC
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteTranslationUseCase: DeleteTranslationUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getHistoryUseCase().collect { translations ->
                _state.value = _state.value.copy(
                    translations = sortTranslations(translations, _state.value.sortOrder)
                )
            }
        }
    }

    fun setSortOrder(sortOrder: SortOrder) {
        _state.value = _state.value.copy(
            sortOrder = sortOrder,
            translations = sortTranslations(_state.value.translations, sortOrder)
        )
    }

    private fun sortTranslations(
        translations: List<Translation>,
        sortOrder: SortOrder
    ): List<Translation> {
        return when (sortOrder) {
            SortOrder.DATE_DESC -> translations.sortedByDescending { it.timestamp }
            SortOrder.DATE_ASC -> translations.sortedBy { it.timestamp }
            SortOrder.SOURCE_TEXT -> translations.sortedBy { it.sourceText.lowercase() }
            SortOrder.TRANSLATED_TEXT -> translations.sortedBy { it.translatedText.lowercase() }
        }
    }

    fun deleteTranslation(translation: Translation) {
        viewModelScope.launch {
            deleteTranslationUseCase(translation)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            clearHistoryUseCase()
        }
    }
}