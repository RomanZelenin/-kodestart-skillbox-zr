package ru.kode.base.internship.products.data.network

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.kode.base.internship.products.data.network.entity.PathAccounts
import ru.kode.base.internship.products.data.network.entity.PathCard
import ru.kode.base.internship.products.data.network.entity.PathDeposit

interface ProductsApi {

  @Headers("Prefer: code=200, example=android")
  @GET("api/core/account/list")
  suspend fun getAccounts(): PathAccounts.Response

  @GET("api/core/card/{id}")
  suspend fun getCard(@Path("id") id: String, @Header("Prefer") prefer:String): PathCard.Response

  @Headers("Prefer: code=200")
  @PUT("api/core/card/{id}")
  suspend fun setCardName(@Path("id") id: String, @Body param:ParamCardName)

  @Headers("Prefer: code=200, example=android")
  @GET("api/core/deposit/list")
  suspend fun getDeposits(): PathDeposit.Response

  @GET("api/core/deposit/{id}")
  suspend fun getDeposit(@Path("id") id:String, @Header("Prefer") prefer:String):PathDeposit.DepositWithTerms
}

@Serializable
data class ParamCardName(val newName: String)