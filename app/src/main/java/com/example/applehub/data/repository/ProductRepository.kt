package com.example.applehub.data.repository

import android.util.Log
import com.example.applehub.common.Resource
import com.example.applehub.data.db.FavoriteDao
import com.example.applehub.data.mapper.mapToFavorite
import com.example.applehub.data.mapper.mapToProduct
import com.example.applehub.data.mapper.mapToProductListUI
import com.example.applehub.data.mapper.mapToProductUI
import com.example.applehub.data.model.request.PostAddToCardBody
import com.example.applehub.data.model.request.PostClearCard
import com.example.applehub.data.model.request.PostDeleteFromCard
import com.example.applehub.data.model.response.Product
import com.example.applehub.data.model.response.ProductListUI
import com.example.applehub.data.model.response.ProductUI
import com.example.applehub.data.source.remote.ProductService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ProductRepository @Inject constructor(private val productService: ProductService, private val favoriteDao: FavoriteDao) {

    suspend fun getProducts(): Resource<List<ProductListUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getProducts().body()

                if (response?.status == 200) {
                    val favoriteProducts = favoriteDao.getAll()
                    Resource.Success(response.products.orEmpty().mapToProductListUI().map {
                        if (favoriteProducts.any { favorite -> favorite.id == it.id }) {
                            it.copy(isFavorite = true)
                        } else {
                            it
                        }
                    })
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "getProducts: ${e.message}")
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun searchProduct(query: String): Resource<List<ProductListUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.searchProduct(query).body()
                val favoriteProducts = favoriteDao.getAll()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapToProductListUI().map {
                        if (favoriteProducts.any { favorite -> favorite.id == it.id }) {
                            it.copy(isFavorite = true)
                        } else {
                            it
                        }
                    })
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "searchProduct: ${e.message}")
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getProductDetail(id: Int): Resource<ProductUI> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getProductDetail(id).body()
                val favoriteProducts = favoriteDao.getAll()



                if (response?.status == 200) {
                    Resource.Success(response.product?.mapToProductUI()?.copy(
                        isFavorite = favoriteProducts.any { favorite -> favorite.id == id })


                    ?: ProductUI.EMPTY)
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "getProductDetail: ${e.message}")
                Resource.Error(e.message.orEmpty())
            }
        }


    suspend fun getCategories(): Resource<List<String>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getCategories().body()

                if (response?.status == 200) {
                    Resource.Success(response.categories.orEmpty())
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "getCategories: ${e.message}")
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getProductByCategory(category: String): Resource<List<ProductListUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favoriteProducts = favoriteDao.getAll()
                val response = productService.getProductsByCategory(category).body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapToProductListUI().map {
                        if (favoriteProducts.any { favorite -> favorite.id == it.id }) {
                            it.copy(isFavorite = true)
                        } else {
                            it
                        }
                    })
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "getProductByCategory: ${e.message}")
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getSalesProducts(): Resource<List<ProductListUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getSaleProducts().body()

                if (response?.status == 200) {
                    val favoriteProducts = favoriteDao.getAll()
                    Resource.Success(response.products.orEmpty().mapToProductListUI().map {
                        if (favoriteProducts.any { favorite -> favorite.id == it.id }) {
                            it.copy(isFavorite = true)
                        } else {
                            it
                        }
                    })
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "getSalesProducts: ${e.message}")
                Resource.Error(e.message.orEmpty())
            }
        }


    suspend fun addToCart(id: Int): Resource<Boolean> = withContext(Dispatchers.IO) {
        try {
            val addToCardBody = PostAddToCardBody(FirebaseAuth.getInstance().uid.orEmpty(), id)
            val response = productService.addToCart(addToCardBody).body()

            if (response?.status == 200) {
                Resource.Success(true)
            } else {
                Resource.Fail(response?.message.orEmpty())
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "addToCart: ${e.message}")
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun getCartProducts(): Resource<List<ProductListUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getCartProducts(FirebaseAuth.getInstance().uid.orEmpty()).body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapToProductListUI())
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Log.e("ProductRepository", "getCartProducts: ${e.message}")
                Resource.Error(e.message.orEmpty())
            }
        }


    suspend fun deleteFromCart(id: Int): Resource<Boolean> = withContext(Dispatchers.IO) {
        try {
            val deleteFromCard = PostDeleteFromCard(id, FirebaseAuth.getInstance().uid.orEmpty())
            val response = productService.deleteFromCart(deleteFromCard).body()

            if (response?.status == 200) {
                Resource.Success(true)
            } else {
                Resource.Fail(response?.message.orEmpty())
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "deleteFromCart: ${e.message}")
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun clearCart(): Resource<Boolean> = withContext(Dispatchers.IO) {
        try {
            val clearCart = PostClearCard( FirebaseAuth.getInstance().uid.orEmpty())
            val response = productService.clearCart(clearCart).body()

            if (response?.status == 200) {
                Resource.Success(true)
            } else {
                Resource.Fail(response?.message.orEmpty())
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "clearCart: ${e.message}")
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun addToFavorite(product: Product ): Resource<Boolean> = withContext(Dispatchers.IO) {
        try {
            favoriteDao.insert(product.mapToFavorite())
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun removeFromFavorite(id: Int): Resource<Boolean> = withContext(Dispatchers.IO) {
        try {
            favoriteDao.deleteById(id)
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun getFavoriteProducts(): Resource<List<ProductListUI>> = withContext(Dispatchers.IO) {
        try {
            val response = favoriteDao.getAll().map{
                it.mapToProduct()
            }.mapToProductListUI()

            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }
}
