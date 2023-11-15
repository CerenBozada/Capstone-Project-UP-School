package com.example.applehub.di

import com.example.applehub.data.db.FavoriteDao
import com.example.applehub.data.repository.ProductRepository
import com.example.applehub.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductsRepository(productService: ProductService, favoriteDao: FavoriteDao) = ProductRepository(productService,favoriteDao)
}