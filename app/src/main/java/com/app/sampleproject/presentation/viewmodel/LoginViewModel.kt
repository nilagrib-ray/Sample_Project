package com.app.sampleproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.usecase.LoginUseCase
import com.app.sampleproject.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }

    fun login() {
        if (!validateInput()) return

        viewModelScope.launch {
            loginUseCase(_uiState.value.email, _uiState.value.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                    is Resource.Success -> {
                        authRepository.saveUserData(resource.data)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        var isValid = true

        if (email.isEmpty()) {
            _uiState.value = _uiState.value.copy(emailError = "Email is required")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(emailError = "Invalid email format")
            isValid = false
        }

        if (password.isEmpty()) {
            _uiState.value = _uiState.value.copy(passwordError = "Password is required")
            isValid = false
        }

        return isValid
    }
}