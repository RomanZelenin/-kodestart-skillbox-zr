package ru.kode.base.internship.products.data.network.entity

import kotlinx.serialization.Serializable

interface PathCard{
  @Serializable
  data class Response(
    val accountId: Int,
    val expiredAt: String,
    val id: Int,
    val name: String,
    val number: String,
    val paymentSystem: String,
    val status: String
  )
}