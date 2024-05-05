package ru.kode.base.internship.core.data.network.di

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.BuildConfig
import ru.kode.base.internship.core.data.network.interceptor.AttachAccessTokenInterceptor
import ru.kode.base.internship.core.data.storage.persistence.TokensPersistence
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
@ContributesTo(AppScope::class)
object NetworkModule {
  @Provides
  @SingleIn(AppScope::class)
  fun provideJson(): Json {
    return Json {
      ignoreUnknownKeys = true
      encodeDefaults = true
      explicitNulls = false
    }
  }

  @Provides
  @SingleIn(AppScope::class)
  fun provideOkHttpClient(tokensPersistence: TokensPersistence): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor { message ->
      Timber.tag("OkHttp")
      Timber.d(message)
    }
    loggingInterceptor.level = HTTP_LOG_LEVEL
    return OkHttpClient.Builder()
      .connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
      .readTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
      .writeTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
      .addNetworkInterceptor(AttachAccessTokenInterceptor(tokensPersistence))
      .addNetworkInterceptor(loggingInterceptor)
      .build()
  }

  @Provides
  @SingleIn(AppScope::class)
  fun provideRetrofit(json: Json, httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
      .client(httpClient)
      .baseUrl("$BASE_URL/")
      .build()
  }

  @Provides
  @ProductsModule
  @SingleIn(AppScope::class)
  fun provideRetrofit2(json: Json, httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
      .client(httpClient)
      .baseUrl("$PRODUCTS_BASE_URL/")
      .build()
  }
}

private const val BASE_URL = "https://stoplight.io/mocks/kode-api/kode-bank/151956"
private const val HTTP_CONNECT_TIMEOUT = 60_000L
private val HTTP_LOG_LEVEL = if (BuildConfig.RELEASE) {
  HttpLoggingInterceptor.Level.BASIC
} else {
  HttpLoggingInterceptor.Level.BODY
}

private const val PRODUCTS_BASE_URL = "https://stoplight.io/mocks/kode-api/kode-bank/6096726"

@Qualifier
annotation class ProductsModule