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
class ShopSearchViewModel  @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchState>(SearchState.Loading)

    val uiState: MutableStateFlow<SearchState> = _uiState

    var lastQuery = ""


    fun searchProducts(query: String) {


        lastQuery = query

        viewModelScope.launch {
            productRepository.searchProduct(query).let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.value = SearchState.SuccessState(resource.data)
                    }
                    is Resource.Fail -> {
                        _uiState.value = SearchState.EmptyScreen(resource.failMessage)
                    }
                    is Resource.Error -> {
                        _uiState.value = SearchState.ShowPopUp(resource.errorMessage)
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
                        searchProducts(lastQuery)
                    }

                    else -> {}
                }
            }
        }



}



}
sealed interface SearchState {
    object Loading : SearchState
    data class SuccessState(val products: List<ProductListUI>) : SearchState
    data class EmptyScreen(val failMessage: String) : SearchState
    data class ShowPopUp(val errorMessage: String) : SearchState
}