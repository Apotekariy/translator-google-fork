package com.example.translator.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.translator.R
import com.example.translator.presentation.details.DetailsScreen
import com.example.translator.presentation.history.HistoryScreen
import com.example.translator.presentation.translator.TranslatorScreen


@Composable
fun TranslatorAppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            TranslatorBottomBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Translator.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Translator.route) {
                TranslatorScreen()
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    onTranslationClick = { translation ->
                        navController.navigate(Screen.Details.createRoute(translation.id))
                    }
                )
            }

            composable(Screen.Details.route) { backStackEntry ->
                val translationId = backStackEntry.arguments?.getString("translationId")?.toLongOrNull()
                if (translationId != null) {
                    DetailsScreen(
                        translationId = translationId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
private fun TranslatorBottomBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_translate_24), contentDescription = "Translate")
            },
            label = { Text("Translator") },
            selected = currentRoute == Screen.Translator.route,
            onClick = {
                navController.navigate(Screen.Translator.route) {
                    popUpTo(Screen.Translator.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(painter = painterResource(id = R.drawable.ic_history_24), contentDescription = "History")
            },
            label = { Text("History") },
            selected = currentRoute == Screen.History.route,
            onClick = {
                navController.navigate(Screen.History.route) {
                    popUpTo(Screen.Translator.route)
                    launchSingleTop = true
                }
            }
        )
    }
}