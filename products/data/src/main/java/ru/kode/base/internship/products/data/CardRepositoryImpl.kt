package ru.kode.base.internship.products.data

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.kode.base.core.di.AppScope
import ru.kode.base.internship.products.domain.CardRepository
import ru.kode.base.internship.products.domain.entity.Card
import ru.kode.base.internship.products.domain.entity.CardStatus
import ru.kode.base.internship.products.domain.entity.CardType
import javax.inject.Inject


@ContributesBinding(AppScope::class)
class CardRepositoryImpl @Inject constructor() : CardRepository {
  override fun cardDetails(id: Card.Id): Flow<Card> {
    return flow { emit(getCardDetails(id)) } }

  override suspend fun getCardDetails(id: Card.Id): Card {
    return listMockCards.first { it.id == id }
  }


  private val listMockCards = listOf(
    Card(
      id = Card.Id("1"),
      title = "Карта зарплатная",
      type = CardType.PHYSICAL,
      status = CardStatus.ACTIVE,
      icon = R.drawable.ic_master_card,
      logo = R.drawable.master_card_logo,
      number = "1234",
      expiryDate = "01/28"
    ),
    Card(
      id = Card.Id("2"),
      title = "Дополнительная карта",
      type = CardType.VIRTUAL,
      status = CardStatus.BLOCKED,
      icon = R.drawable.ic_visa_card,
      logo = R.drawable.visa_card_logo,
      number = "2341",
      expiryDate = "02/25"
    ),
    Card(
      id = Card.Id("3"),
      title = "Дополнительная карта",
      status = CardStatus.ACTIVE,
      type = CardType.VIRTUAL,
      icon = R.drawable.ic_visa_card,
      logo = R.drawable.visa_card_logo,
      number = "7721",
      expiryDate = "08/30"
    ),
  )
}