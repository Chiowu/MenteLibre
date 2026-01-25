package com.example.mentelibre.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentelibre.data.mood.MoodRepository
import com.example.mentelibre.data.mood.MoodResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MoodRepository
) : ViewModel() {

    private val _todayMood = MutableStateFlow<MoodResult?>(null)
    val todayMood: StateFlow<MoodResult?> = _todayMood

    fun loadTodayMood() {
        viewModelScope.launch {
            _todayMood.value = repository.getTodayMood()
        }
    }
}
