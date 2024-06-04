package com.github.gasblg.stockshelper.network.di

import com.github.gasblg.stockshelper.network.BuildConfig
import com.github.gasblg.stockshelper.network.api.ApiExchange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        coerceInputValues = true
        isLenient = true
        ignoreUnknownKeys = true
        allowStructuredMapKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(): ApiExchange = Retrofit.Builder()
        .baseUrl("http://192.168.1.75:8080")
        .callFactory(okHttpCallFactory())
        .addConverterFactory(
            providesNetworkJson().asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(ApiExchange::class.java)

}
