package ru.kode.base.internship.products.data.network.entity

import kotlinx.serialization.Serializable


interface PathAccounts {
  @Serializable
  data class Response(
    val accounts: List<Account>,
  )

  @Serializable
  data class Account(
    val accountId: Int,
    val balance: Int,
    val cards: List<Card>,
    val currency: String,
    val number: String,
    val status: String,
  )

  @Serializable
  data class Card(
    val card_id: String,
    val card_type: String,
    val name: String,
    val number: String,
    val payment_system: String,
    val status: String,
  )
}
