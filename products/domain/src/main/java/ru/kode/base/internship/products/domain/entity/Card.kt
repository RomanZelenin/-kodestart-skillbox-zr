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
  @DrawableRes val icon: Int,
) {
  @JvmInline
  value class Id(val value: String)
}

enum class CardStatus(
  @StringRes val id: Int
) {
  ACTIVE(R.string.active),
  BLOCKED(R.string.blocked),
}

enum class CardType(
  @StringRes val id: Int
) {
  PHYSICAL(R.string.physical),
  VIRTUAL(R.string.virtual)
}
