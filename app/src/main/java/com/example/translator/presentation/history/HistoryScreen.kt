package com.example.translator.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.translator.R
import com.example.translator.domain.model.Translation
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onTranslationClick: (Translation) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Box {
                TopAppBar(
                    title = { Text("History") },
                    actions = {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_sort_24),
                                contentDescription = "Sort"
                            )
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Newest first") },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.DATE_DESC)
                                    showSortMenu = false
                                },
                                trailingIcon = {
                                    if (state.sortOrder == SortOrder.DATE_DESC) {
                                        Text("✓", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Oldest first") },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.DATE_ASC)
                                    showSortMenu = false
                                },
                                trailingIcon = {
                                    if (state.sortOrder == SortOrder.DATE_ASC) {
                                        Text("✓", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Source text (A-Z)") },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.SOURCE_TEXT)
                                    showSortMenu = false
                                },
                                trailingIcon = {
                                    if (state.sortOrder == SortOrder.SOURCE_TEXT) {
                                        Text("✓", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Translation (A-Z)") },
                                onClick = {
                                    viewModel.setSortOrder(SortOrder.TRANSLATED_TEXT)
                                    showSortMenu = false
                                },
                                trailingIcon = {
                                    if (state.sortOrder == SortOrder.TRANSLATED_TEXT) {
                                        Text("✓", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            )
                        }


                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete_24),
                                contentDescription = "Clear history"
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        if (state.translations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No translations yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.translations,
                    key = { it.id }
                ) { translation ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteTranslation(translation)
                                true
                            } else {
                                false
                            }
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_delete_24),
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true
                    ) {
                        TranslationItem(
                            translation = translation,
                            onClick = { onTranslationClick(translation) }
                        )
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear history") },
            text = { Text("Are you sure you want to delete all translations?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TranslationItem(
    translation: Translation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = translation.sourceText,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = translation.translatedText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${translation.sourceLang} → ${translation.targetLang} • ${formatDate(translation.timestamp)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}