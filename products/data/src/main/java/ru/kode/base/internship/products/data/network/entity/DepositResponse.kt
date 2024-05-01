package ru.kode.base.internship.products.data.network.entity

import kotlinx.serialization.Serializable

interface PathDeposit {
  @Serializable
  data class Response(
    val deposits: List<Deposit>,
  )

  @Serializable
  data class Deposit(
    val balance: Int,
    val currency: String,
    val depositId: Int,
    val name: String,
    val status: String,
  )

  @Serializable
  data class DepositWithTerms(
    val balance: Int,
    val closeDate: String,
    val currency: String,
    val name: String,
    val rate: Double,
    val status: String
  )
}