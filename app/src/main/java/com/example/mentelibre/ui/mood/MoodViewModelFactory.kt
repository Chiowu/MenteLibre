package com.example.mentelibre.ui.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mentelibre.data.mood.MoodRepository

class MoodViewModelFactory(
    private val repository: MoodRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
