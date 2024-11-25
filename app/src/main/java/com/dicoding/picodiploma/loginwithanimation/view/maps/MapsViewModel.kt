package com.dicoding.picodiploma.loginwithanimation.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    private val _storyResult = MutableLiveData<List<ListStoryItem>>()
    val storyResult: LiveData<List<ListStoryItem>> = _storyResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStoryWithLocation() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = repository.getSession().first().token
                val storyResponse = repository.getStoryWithLocation("Bearer $token")

                val nonNullStoryList = storyResponse.listStory.filterNotNull() ?: emptyList()

                _storyResult.value = nonNullStoryList
            } catch (e: Exception) {
                // Tidak menampilkan pesan error
            } finally {
                _isLoading.value = false
            }
        }
    }
}