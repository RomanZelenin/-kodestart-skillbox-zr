package ru.kode.base.internship.products.domain

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.entity.Card

interface CardRepository {
  fun cardDetails(id: Card.Id): Flow<Card>

  suspend fun getCardDetails(id: Card.Id):Card
}
