package com.example.translator.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApi {
    @GET("translate_a/t")
    suspend fun translate(
        @Query("client") client : String = "dict-chrome-ex",
        @Query("sl") sourceLang: String,
        @Query("tl") targetLang: String,
        @Query("q") text: String
    ) : List<String>
}