package com.example.translator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    @Query("SELECT * FROM translations WHERE sourceText = :text AND :sourceLang = :sourceLang AND targetLang = :targetLang LIMIT 1")
    suspend fun getTranslation(text: String, sourceLang: String, targetLang: String): TranslationEntity?

    @Query("SELECT * FROM translations ORDER BY timestamp DESC")
    fun getAllTranslations(): Flow<List<TranslationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity)

    @Delete
    suspend fun deleteTranslation(translation: TranslationEntity)

    @Query("DELETE FROM translations")
    suspend fun clearAll()
}