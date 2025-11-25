package com.example.translator.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.translator.R

import com.example.translator.presentation.history.HistoryViewModel
import com.example.translator.presentation.history.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    translationId: Long,
    onNavigateBack: () -> Unit,
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val state by historyViewModel.state.collectAsState()
    val translation = state.translations.find { it.id == translationId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Translation Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(painter = painterResource(R.drawable.ic_arrow_back_24), contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (translation != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard(
                    title = "Source Text",
                    content = translation.sourceText
                )

                DetailCard(
                    title = "Translation",
                    content = translation.translatedText
                )

                DetailCard(
                    title = "Languages",
                    content = "${translation.sourceLang.uppercase()} → ${translation.targetLang.uppercase()}"
                )

                DetailCard(
                    title = "Date",
                    content = formatDate(translation.timestamp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Translation not found")
            }
        }
    }
}

@Composable
fun DetailCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}