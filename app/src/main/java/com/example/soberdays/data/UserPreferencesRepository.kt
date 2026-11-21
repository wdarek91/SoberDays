package com.example.soberdays.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_settings")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val START_TIMESTAMP = longPreferencesKey("start_timestamp")
        val DAILY_COST = doublePreferencesKey("daily_cost")
    }

    val startTimestampFlow: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[START_TIMESTAMP] ?: System.currentTimeMillis()
    }

    val dailyCostFlow: Flow<Double> = context.dataStore.data.map { preferences ->
        preferences[DAILY_COST] ?: 25.0
    }

    suspend fun saveStartDate(timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[START_TIMESTAMP] = timestamp
        }
    }

    suspend fun saveDailyCost(cost: Double) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_COST] = cost
        }
    }

    suspend fun resetCounter() {
        context.dataStore.edit { preferences ->
            preferences[START_TIMESTAMP] = System.currentTimeMillis()
        }
    }
}
