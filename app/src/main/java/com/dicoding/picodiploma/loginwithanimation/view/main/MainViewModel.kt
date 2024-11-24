package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    private val _userToken = MediatorLiveData<String>()
    val userToken: LiveData<String> get() = _userToken

    init {
        _userToken.addSource(repository.getSession().asLiveData()) { userModel ->
            _userToken.value = userModel.token
        }
    }

    fun getStory() {
        userToken.observeForever { token ->
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    repository.getStoriesWithPaging(token).observeForever { pagingData ->
                        _stories.value = pagingData
                    }
                } catch (e: Exception) {
                    // Tangani error jika ada
                } finally {
                    _isLoading.value = false
                }
            }
        }

    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}