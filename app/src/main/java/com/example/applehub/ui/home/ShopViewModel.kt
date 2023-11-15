package com.example.applehub.ui.home

import androidx.lifecycle.ViewModel
import com.example.applehub.data.model.response.ProductListUI
import com.example.applehub.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.applehub.common.Resource
import com.example.applehub.data.mapper.mapToProduct
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel  @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)

    val uiState: MutableStateFlow<HomeState> = _uiState


    private val _salesState = MutableStateFlow<List<ProductListUI>>(emptyList())

    val salesState: MutableStateFlow<List<ProductListUI>> = _salesState

    val categoryState = MutableStateFlow<List<String>>(emptyList())

    init {
        getProducts()
        getCategories()
        getSales()
    }

    private fun getProducts() {




        viewModelScope.launch {
            productRepository.getProducts().let { resource ->
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


    private fun getCategories() {
        viewModelScope.launch {
            productRepository.getCategories().let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        categoryState.value = resource.data
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

    fun getSales() {
        viewModelScope.launch {
            productRepository.getSalesProducts().let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _salesState.value = resource.data
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

    fun getProductByCategory(category: String) {
        viewModelScope.launch {
            productRepository.getProductByCategory(category).let { resource ->
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

    fun favoriteProduct(id: Int) {
        viewModelScope.launch {
            productRepository.getProductDetail(id).let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (resource.data.isFavorite) {
                            productRepository.removeFromFavorite(id)
                        } else {
                            productRepository.addToFavorite(resource.data.mapToProduct())
                        }
                        getProducts()
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


sealed interface HomeState {
    object Loading : HomeState
    data class SuccessState(val products: List<ProductListUI>) : HomeState
    data class EmptyScreen(val failMessage: String) : HomeState
    data class ShowPopUp(val errorMessage: String) : HomeState
}