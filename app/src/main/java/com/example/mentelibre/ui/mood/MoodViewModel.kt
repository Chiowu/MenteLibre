package com.example.mentelibre.ui.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mentelibre.data.mood.MoodRepository
import com.example.mentelibre.data.mood.MoodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoodViewModel(
    private val repository: MoodRepository
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow<MoodPeriod>(MoodPeriod.DAYS)
    val selectedPeriod: StateFlow<MoodPeriod> = _selectedPeriod

    private val _chartData = MutableStateFlow<List<MoodChartPoint>>(emptyList())
    val chartData: StateFlow<List<MoodChartPoint>> = _chartData

    init {
        loadChart()
    }

    fun changePeriod(period: MoodPeriod) {
        _selectedPeriod.value = period
        loadChart()
    }

    private fun loadChart() {
        viewModelScope.launch {
            _chartData.value = when (_selectedPeriod.value) {
                MoodPeriod.DAYS -> repository.getLast7DaysChart()
                MoodPeriod.WEEKS -> repository.getLast30DaysChart()
                MoodPeriod.MONTHS -> repository.getLast30DaysChart()
            }
        }
    }

    fun saveMood(mood: MoodType) {
        viewModelScope.launch {
            repository.saveMood(mood)
            loadChart()
        }
    }
}
