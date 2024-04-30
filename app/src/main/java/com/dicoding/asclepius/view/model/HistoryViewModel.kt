package com.dicoding.asclepius.view.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class HistoryViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllHistory(): LiveData<List<HistoryEntity>> {
        _isLoading.value = true
        return mHistoryRepository.getAllHistory().apply {
            _isLoading.value = false
        }
    }

    fun deleteHistory(history: HistoryEntity) {
        mHistoryRepository.delete(history)
    }
}
