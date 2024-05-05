package ru.kode.base.internship.products.data.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kode.base.core.annotations.ApplicationContext
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.data.network.di.ProductsModule
import ru.kode.base.internship.products.data.network.ProductsApi
import ru.kode.base.internship.products.data.storage.ProductsDatabase
import timber.log.Timber

@Module
@ContributesTo(AppScope::class)
object ProductsDataModule {

  @Provides
  @SingleIn(AppScope::class)
  fun provideProductsApi(@ProductsModule retrofit: Retrofit): ProductsApi {
    return retrofit.create(ProductsApi::class.java)
  }

  @Provides
  @SingleIn(AppScope::class)
  fun provideProductsDb(@ApplicationContext context: Context): ProductsDatabase {
    val driver = AndroidSqliteDriver(ProductsDatabase.Schema, context, null)
      .let { driver ->
        if (ENABLE_LOGGING) {
          LogSqliteDriver(driver) { log ->
            Timber.e(log)
          }
        } else {
          driver
        }
      }
    return ProductsDatabase(driver)
  }
}

private const val ENABLE_LOGGING = false