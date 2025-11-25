package com.example.translator.presentation.translator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.translator.R
import com.example.translator.domain.model.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Translator") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LanguageSelector(
                    label = "From",
                    selectedLanguage = state.sourceLang,
                    languages = viewModel.availableLanguages,
                    onLanguageSelected = viewModel::onSourceLangChange
                )

                IconButton(
                    onClick = viewModel::swapLanguages,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(painter = painterResource(R.drawable.ic_swap_vert_24), contentDescription = "Swap languages")
                }

                LanguageSelector(
                    label = "To",
                    selectedLanguage = state.targetLang,
                    languages = viewModel.availableLanguages,
                    onLanguageSelected = viewModel::onTargetLangChange
                )

                OutlinedTextField(
                    value = state.sourceText,
                    onValueChange = viewModel::onSourceTextChange,
                    label = { Text("Enter text") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Button(
                    onClick = viewModel::translate,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.sourceText.isNotBlank() && !state.isLoading
                ) {
                    Text("Translate")
                }

                if (state.translatedText.isNotBlank()) {
                    OutlinedTextField(
                        value = state.translatedText,
                        onValueChange = {},
                        label = { Text("Translation") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        minLines = 3
                    )
                }

                if (state.error != null) {
                    Text(
                        text = "Error: ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    label: String,
    selectedLanguage: Language,
    languages: List<Language>,
    onLanguageSelected: (Language) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        val fillMaxWidth = Modifier
            .fillMaxWidth()
        OutlinedTextField(
            value = selectedLanguage.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = fillMaxWidth.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.name) },
                    onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    }
                )
            }
        }
    }
}