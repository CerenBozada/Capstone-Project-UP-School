package com.example.applehub.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState(isLoading = false))
    val uiState: StateFlow<LoginState> = _uiState


    init {
        _uiState.value = LoginState(isLoading = false)
    }

     fun login(username: String, password: String) {
        val auth= FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.value = LoginState(isLoginSuccess = true)
                } else {
                    _uiState.value = LoginState(isLoginSuccess = false)
                }
            }

    }

}

data class LoginState(
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
)