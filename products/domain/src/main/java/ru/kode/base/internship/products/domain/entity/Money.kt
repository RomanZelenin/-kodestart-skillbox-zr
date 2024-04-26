package ru.kode.base.internship.products.domain.entity

import androidx.compose.runtime.Immutable

@Immutable
data class Money(
  val amount: String,
  val sign: CurrencySign,
)
