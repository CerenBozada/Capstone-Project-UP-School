package com.example.applehub.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applehub.common.Resource
import com.example.applehub.data.model.response.ProductListUI
import com.example.applehub.data.model.response.ProductUI
import com.example.applehub.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel  @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailState>(DetailState.Loading)

    val uiState: MutableStateFlow<DetailState> = _uiState



    fun getProductDetail(id: Int) {
        viewModelScope.launch {
            productRepository.getProductDetail(id).let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.value = DetailState.SuccessState(resource.data)
                    }
                    is Resource.Fail -> {
                        _uiState.value = DetailState.EmptyScreen(resource.failMessage)
                    }
                    is Resource.Error -> {
                        _uiState.value = DetailState.ShowPopUp(resource.errorMessage)
                    }
                }
            }
        }

    }

    fun addToCart(id: Int) {
        viewModelScope.launch {
            productRepository.addToCart(id).let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val currentState = _uiState.value as DetailState.SuccessState
                        _uiState.value = currentState.copy(isAdded = true)
                    }
                    is Resource.Fail -> {
                        _uiState.value = DetailState.EmptyScreen(resource.failMessage)
                    }
                    is Resource.Error -> {
                        _uiState.value = DetailState.ShowPopUp(resource.errorMessage)
                    }
                }
            }
        }

    }



}


sealed interface DetailState {
    object Loading : DetailState
    data class SuccessState(val product: ProductUI, val isAdded: Boolean = false) : DetailState
    data class EmptyScreen(val failMessage: String) : DetailState
    data class ShowPopUp(val errorMessage: String) : DetailState
}