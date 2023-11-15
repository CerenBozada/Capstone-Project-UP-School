package com.example.applehub.data.source.remote

import com.example.applehub.data.model.request.PostAddToCardBody
import com.example.applehub.data.model.request.PostClearCard
import com.example.applehub.data.model.request.PostDeleteFromCard
import com.example.applehub.data.model.response.GeneralResponse
import com.example.applehub.data.model.response.GetCategoriesResponse
import com.example.applehub.data.model.response.GetProductDetailResponse
import com.example.applehub.data.model.response.GetProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ProductService {

    @GET("get_products.php")
    suspend fun getProducts(): Response<GetProductsResponse>

    @GET("get_product_detail.php")
    suspend fun getProductDetail(@Query("id") id: Int): Response<GetProductDetailResponse>

    @GET("search_product.php")
    suspend fun searchProduct(@Query("query") query: String): Response<GetProductsResponse>

    @GET("get_products_by_category.php")
    suspend fun getProductsByCategory(@Query("category") category: String): Response<GetProductsResponse>

    @GET("get_sale_products.php")
    suspend fun getSaleProducts(): Response<GetProductsResponse>

    @GET("get_cart_products.php")
    suspend fun getCartProducts(@Query("userId") userId: String): Response<GetProductsResponse>


    @GET("get_categories.php")
    suspend fun getCategories(): Response<GetCategoriesResponse>

    @POST("add_to_cart.php")
    suspend fun addToCart(@Body body: PostAddToCardBody): Response<GeneralResponse>

    @POST("delete_from_cart.php")
    suspend fun deleteFromCart(@Body body: PostDeleteFromCard): Response<GeneralResponse>

    @POST("clear_cart.php")
    suspend fun clearCart(@Body body: PostClearCard): Response<GeneralResponse>

}