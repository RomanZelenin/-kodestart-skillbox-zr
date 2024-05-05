package ru.kode.base.internship.products.data

import ru.kode.base.internship.products.data.network.entity.PathAccounts
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.domain.entity.CardStatus
import ru.kode.base.internship.products.domain.entity.CardType
import ru.kode.base.internship.products.domain.entity.CurrencySign


internal fun PathAccounts.Card.toStorageModel(accountId: Long, expiredAt: String): products.data.account.Card {
  return products.data.account.Card(
    id = card_id.toLong(),
    name = name,
    number = number,
    status = status,
    accountId = accountId,
    paymentSystem = payment_system,
    expiredAt = expiredAt,
    type = card_type
  )
}

internal fun PathAccounts.Account.toStorageModel(): products.data.account.Account {
  return products.data.account.Account(
    id = accountId.toLong(),
    title = number,
    money = balance.toString(),
    country_currency = currency,
    status = status,
  )
}

internal fun products.data.account.Card.toDomainModel(): Card {
  return Card(
    id = Card.Id(id.toString()),
    title = name,
    status = when (status.lowercase()) {
      "active" -> CardStatus.ACTIVE
      else -> CardStatus.BLOCKED
    },
    type = when (type.lowercase()) {
      "physical" -> CardType.PHYSICAL
      else -> CardType.VIRTUAL
    },
    expiredAt = expiredAt,
    number = number,
    logo = when (paymentSystem.lowercase()) {
      "visa" -> {
        R.drawable.visa_card_logo
      }

      else -> {
        R.drawable.master_card_logo
      }
    },
    icon = when (paymentSystem.lowercase()) {
      "visa" -> {
        R.drawable.ic_visa_card
      }

      else -> {
        R.drawable.ic_master_card
      }
    }
  )
}

internal fun String.toCurrencySign(): CurrencySign {
  return when (lowercase()) {
    "rub" -> CurrencySign.RUB
    "usd" -> CurrencySign.USD
    else -> CurrencySign.EUR
  }
}