package com.example.translator.navigation

sealed class Screen(val route: String) {
    data object Translator : Screen("translator")
    data object History : Screen("history")
    data object Details : Screen("details/{translationId}") {
        fun createRoute(translationId: Long) = "details/$translationId"
    }
}