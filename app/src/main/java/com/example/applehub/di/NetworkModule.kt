package com.example.applehub.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.applehub.common.Constants.BASE_URL
import com.example.applehub.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context) = ChuckerInterceptor.Builder(context).collector(
        ChuckerCollector(
            context = context,
            showNotification = true,        // Boolean for showing Notification, set to true to show and false otherwise

        )
    ).alwaysReadResponseBody(true).build()

    @Singleton
    @Provides
    fun provideOkHttp(chucker: ChuckerInterceptor) = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("store", "cerenbozada")

                // log response to Logcat




                return@Interceptor chain.proceed(builder.build())
            }
        )

        addInterceptor(chucker)


    }.build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder().apply {
        addConverterFactory(GsonConverterFactory.create())
        baseUrl(BASE_URL)
        client(okHttpClient)
    }.build()

    @Singleton
    @Provides
    fun provideProductService(retrofit: Retrofit) = retrofit.create(ProductService::class.java)
}