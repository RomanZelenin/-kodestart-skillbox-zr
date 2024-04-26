package ru.kode.base.internship.products.domain.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ru.kode.base.core.util.randomUuid
import ru.kode.base.internship.products.domain.R

@Immutable
data class Card(
  val id: Id = Id(randomUuid()),
  val title: String,
  val status: CardStatus,
  val type: CardType,
  val expiryDate: String,
  val number: String,
  @DrawableRes val icon: Int,
  @DrawableRes val logo: Int,
) {
  @JvmInline
  value class Id(val value: String)
}

enum class CardStatus {
  ACTIVE,
  BLOCKED,
}

enum class CardType {
  PHYSICAL,
  VIRTUAL
}
