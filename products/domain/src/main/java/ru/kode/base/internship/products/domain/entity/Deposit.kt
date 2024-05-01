package ru.kode.base.internship.products.domain.entity

import androidx.compose.runtime.Immutable
import ru.kode.base.core.util.randomUuid

@Immutable
data class Deposit(
  val id: Id = Id(randomUuid()),
  val title: String,
  val money: Money,
  val term: DepositTerms,
) {
  @JvmInline
  value class Id(val value: String)
}

@Immutable
data class DepositTerms(
  val id: Id = Id(randomUuid()),
  val rate: String,
  val date: String,
) {
  @JvmInline
  value class Id(val value: String)
}
