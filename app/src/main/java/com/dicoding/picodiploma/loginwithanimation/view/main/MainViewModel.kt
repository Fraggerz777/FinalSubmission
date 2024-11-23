package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _stories = MutableLiveData<PagingData<ListStoryItem>>()
    val stories: LiveData<PagingData<ListStoryItem>> get() = _stories

    private val _isLoading = MutableLiveData<Boolean>()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = repository.getSession().first().token
                repository.getStoriesWithPaging(token).collect { pagingData ->
                    _stories.value = pagingData
                }
            } catch (e: Exception) {
                // Tangani error jika diperlukan
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}