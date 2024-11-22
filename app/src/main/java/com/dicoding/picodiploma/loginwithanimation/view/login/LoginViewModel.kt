package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String?>()
    val isError: MutableLiveData<String?> get() = _isError

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse


    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                val loginResult = response.loginResult


                val userId = loginResult?.userId ?: ""
                val name = loginResult?.name ?: ""
                val token = loginResult?.token ?: ""

                val user = UserModel(
                    userId = userId,
                    name = name,
                    email = email,
                    token = token,
                    isLogin = true
                )
                saveAuth(user)
                _loginResponse.postValue(response)
            } catch (e: HttpException) {
                httpException(e)
            } catch (e: Exception) {
                generalException(e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
    private fun saveAuth(userModel: UserModel) {
        viewModelScope.launch {
            repository.saveAuth(userModel)
        }
    }
    private fun httpException(e: HttpException) {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
        _isError.postValue(errorBody.message)
    }
    private fun generalException(e: Exception) {
        _isError.postValue(e.message ?: "An unexpected error occurred")
    }
}