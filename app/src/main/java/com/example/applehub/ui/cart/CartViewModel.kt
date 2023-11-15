package com.example.applehub.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applehub.common.Resource
import com.example.applehub.data.model.response.ProductListUI
import com.example.applehub.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel  @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CartState>(CartState.Loading)

    val uiState: MutableStateFlow<CartState> = _uiState



    init {
        getCartItems()
    }


    fun getCartItems() {


        viewModelScope.launch {
            productRepository.getCartProducts().let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.value = CartState.SuccessState(resource.data)
                    }
                    is Resource.Fail -> {
                        _uiState.value = CartState.EmptyScreen(resource.failMessage)
                    }

                    is Resource.Error -> TODO()
                }
            }
        }

    }

    fun deleteFromCard(id: Int) {
        viewModelScope.launch {
            productRepository.deleteFromCart(id).let { resource ->
                Log.e("CartViewModel", "deleteFromCard: ${id}")
                getCartItems()
            }
        }
    }

    fun confirmCart(price:Float) {
        uiState.value = CartState.CheckOut(price)
    }

    fun doneTransaction() {
        viewModelScope.launch {
            productRepository.clearCart().let { resource ->
                uiState.value = CartState.TransactionDone("Ödeme tamamlandı, ürünleriniz en kısa sürede kargoya verilecektir.")
            }
        }
    }



}


sealed interface CartState {
    object Loading : CartState
    data class SuccessState(val products: List<ProductListUI>) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class CheckOut(val totalPrice: Float) : CartState

    data class TransactionDone(val message: String) : CartState
}