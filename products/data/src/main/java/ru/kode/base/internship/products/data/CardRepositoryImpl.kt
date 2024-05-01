package ru.kode.base.internship.products.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.products.data.network.ParamCardName
import ru.kode.base.internship.products.data.network.ProductsApi
import ru.kode.base.internship.products.data.storage.ProductsDatabase
import ru.kode.base.internship.products.domain.CardRepository
import ru.kode.base.internship.products.domain.entity.Card
import javax.inject.Inject

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class CardRepositoryImpl @Inject constructor(
  private val productsApi: ProductsApi,
  private val productsDatabase: ProductsDatabase,
) : CardRepository {

  override val cards: Flow<List<Card>>
    get() = productsDatabase.cardQueries.selectAll()
      .asFlow()
      .mapToList(Dispatchers.IO)
      .map { it.map { it.toDomainModel() } }


  override suspend fun editCardName(id: Card.Id, name: String) {
    productsApi.setCardName(id = id.value, param = ParamCardName(name))
    productsDatabase.cardQueries.changeCardName(name, id.value.toLong())
  }


}