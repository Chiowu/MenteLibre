package com.example.mentelibre.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentelibre.data.mood.MoodRepository
import com.example.mentelibre.data.mood.MoodResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: MoodRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<MoodResult>>(emptyList())
    val history: StateFlow<List<MoodResult>> = _history

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getAllMoods()
        }
    }
}
