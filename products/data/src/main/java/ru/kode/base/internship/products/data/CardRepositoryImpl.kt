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
    return flow {
      listMockCards.find { it.id == id }?.let {
        emit(it)
      }
    }
  }


  private val listMockCards = listOf(
    Card(
      id = Card.Id("1"),
      title = "Карта зарплатная",
      type = CardType.PHYSICAL,
      status = CardStatus.ACTIVE,
      icon = R.drawable.ic_master_card
    ),
    Card(
      id = Card.Id("2"),
      title = "Дополнительная карта",
      type = CardType.VIRTUAL,
      status = CardStatus.BLOCKED,
      icon = R.drawable.ic_visa_card
    ),
    Card(
      id = Card.Id("3"),
      title = "Дополнительная карта",
      status = CardStatus.ACTIVE,
      type = CardType.VIRTUAL,
      icon = R.drawable.ic_visa_card
    ),
  )
}