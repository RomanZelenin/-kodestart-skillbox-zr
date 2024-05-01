package ru.kode.base.internship.products.domain

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.entity.Card

interface CardRepository {

  val cards:Flow<List<Card>>
  suspend fun editCardName(id: Card.Id, name: String)

}
