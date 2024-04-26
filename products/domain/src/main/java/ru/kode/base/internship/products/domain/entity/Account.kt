package ru.kode.base.internship.products.domain.entity

import androidx.compose.runtime.Immutable
import ru.kode.base.core.util.randomUuid

@Immutable
data class Account(
  val id: Id = Id(randomUuid()),
  val title: String,
  val money: Money,
  val attachedCards: List<Card.Id>,
) {
  @JvmInline
  value class Id(val value: String)
}
