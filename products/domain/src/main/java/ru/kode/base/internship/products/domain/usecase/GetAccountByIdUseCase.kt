package ru.kode.base.internship.products.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kode.base.core.di.AppScope
import ru.kode.base.core.di.SingleIn
import ru.kode.base.internship.core.domain.BaseUseCase
import ru.kode.base.internship.products.domain.AccountsRepository
import ru.kode.base.internship.products.domain.entity.Account
import javax.inject.Inject

@SingleIn(AppScope::class)
class GetAccountByIdUseCase @Inject constructor(private val accountsRepository: AccountsRepository) :
  BaseUseCase<GetAccountByIdUseCase.State>(State()) {
  data class State(
    val account: Account? = null,
  )

  val account: Flow<Account?>
    get() = stateFlow.map { it.account }.distinctUntilChanged()

  suspend operator fun invoke(id: Account.Id) {
    val account = accountsRepository.accounts.first().first { it.id == id }
    setState { copy(account = account) }
  }
}