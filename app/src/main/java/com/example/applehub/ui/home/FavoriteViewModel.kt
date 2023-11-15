package com.example.applehub.ui.home

import androidx.lifecycle.ViewModel
import com.example.applehub.data.model.response.ProductListUI
import com.example.applehub.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.applehub.common.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel  @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)

    val uiState: MutableStateFlow<HomeState> = _uiState

    init {
        getProducts()
    }

    private fun getProducts() {




        viewModelScope.launch {
            productRepository.getFavoriteProducts().let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.value = HomeState.SuccessState(resource.data)
                    }
                    is Resource.Fail -> {
                        _uiState.value = HomeState.EmptyScreen(resource.failMessage)
                    }
                    is Resource.Error -> {
                        _uiState.value = HomeState.ShowPopUp(resource.errorMessage)
                    }
                }
            }
        }
    }
    }

