package com.example.soberdays.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.soberdays.data.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class SoberUiState(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val savedMoney: Double = 0.0,
    val dailyCost: Double = 25.0,
    val quote: String = "Najlepszy czas na zasadzenie drzewa był 20 lat temu. Drugi najlepszy czas jest teraz."
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(SoberUiState())
    val uiState: StateFlow<SoberUiState> = _uiState.asStateFlow()

    private val quotes = listOf(
        "Kto opanował samego siebie, jest potężniejszy niż zdobywca miast. — Seneka",
        "Masz władzę nad swoim umysłem, a nie nad zdarzeniami zewnętrznymi. — Marek Aureliusz",
        "Nie jest wolny ten, kto nie panuje nad sobą. — Pitagoras",
        "Cierpienie po dyscyplinie waży gramy, żal po odpuszczeniu waży tony.",
        "Każdy dzień to nowa szansa na bycie lepszą wersją siebie."
    )

    init {
        viewModelScope.launch {
            combine(repository.startTimestampFlow, repository.dailyCostFlow) { startMs, dailyCost ->
                Pair(startMs, dailyCost)
            }.collect { (startMs, dailyCost) ->
                updateCalculations(startMs, dailyCost)
            }
        }

        viewModelScope.launch {
            while (true) {
                delay(1000 * 30)
                val current = _uiState.value
                _uiState.value = current.copy()
            }
        }
    }

    private fun updateCalculations(startMs: Long, dailyCost: Double) {
        val now = System.currentTimeMillis()
        val diffMs = (now - startMs).coerceAtLeast(0)

        val days = TimeUnit.MILLISECONDS.toDays(diffMs)
        val hours = TimeUnit.MILLISECONDS.toHours(diffMs) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMs) % 60

        val daysFraction = diffMs.toDouble() / (1000.0 * 60.0 * 60.0 * 24.0)
        val savedMoney = daysFraction * dailyCost

        _uiState.value = SoberUiState(
            days = days,
            hours = hours,
            minutes = minutes,
            savedMoney = savedMoney,
            dailyCost = dailyCost,
            quote = quotes.random()
        )
    }

    fun reset() {
        viewModelScope.launch {
            repository.resetCounter()
        }
    }

    fun updateDailyCost(cost: Double) {
        viewModelScope.launch {
            repository.saveDailyCost(cost)
        }
    }
}
