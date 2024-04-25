package ru.kode.base.internship.products.ui.home

import kotlinx.coroutines.flow.MutableSharedFlow
import ru.dimsuz.unicorn2.Machine
import ru.dimsuz.unicorn2.machine
import ru.kode.base.core.BaseViewModel
import ru.kode.base.internship.products.domain.usecase.FetchAccountsUseCase
import ru.kode.base.internship.products.domain.usecase.LoadingCardsByIdAccountUseCase
import ru.kode.base.internship.products.domain.usecase.FetchDepositsUseCase
import ru.kode.base.internship.products.domain.usecase.FetchProductsUseCase
import ru.kode.base.internship.routing.FlowEvent
import javax.inject.Inject

class ProductsHomeViewModel @Inject constructor(
  private val flowEvents: MutableSharedFlow<FlowEvent>,
  private val fetchAccountsUseCase: FetchAccountsUseCase,
  private val loadingCardsByIdAccountUseCase: LoadingCardsByIdAccountUseCase,
  private val fetchDepositsUseCase: FetchDepositsUseCase,
  private val fetchProductsUseCase: FetchProductsUseCase,
) : BaseViewModel<ProductsHomeViewState, ProductsHomeIntents>() {
  override fun buildMachine(): Machine<ProductsHomeViewState> {
    return machine {
      initial = ProductsHomeViewState() to {
        fetchAccountsUseCase()
        fetchDepositsUseCase()
      }

      onEach(intent(ProductsHomeIntents::refresh)) {
        transitionTo { state, _ -> state.copy(listExpandedAccounts = emptyList()) }
        action { _, _, _ -> executeAsync { fetchProductsUseCase() } }
      }

      onEach(fetchProductsUseCase.isRefreshing) {
        transitionTo { state, isRefreshing -> state.copy(isRefreshing = isRefreshing) }
      }


      onEach(fetchAccountsUseCase.accountState) {
        transitionTo { state, lceState -> state.copy(accountsLceState = lceState) }
      }

      onEach(fetchAccountsUseCase.accounts) {
        transitionTo { state, accountList -> state.copy(loadedAccounts = accountList) }
      }

      onEach(intent(ProductsHomeIntents::loadAccounts)) {
        action { _, _, _ -> executeAsync { fetchAccountsUseCase() } }
      }

      onEach(intent(ProductsHomeIntents::expandAccount)) {
        transitionTo { state, pair ->
          if (state.listExpandedAccounts.contains(pair.first)) {
            state.copy(listExpandedAccounts = state.listExpandedAccounts.filter { it != pair.first })
          } else {
            state.copy(listExpandedAccounts = ArrayList(state.listExpandedAccounts).apply { add(pair.first) })
          }
        }
        action { state, _, pair ->
          if (!state.listExpandedAccounts.contains(pair.first)) {
            loadingCardsByIdAccountUseCase(pair.first, pair.second)
          }
        }
      }

      onEach(loadingCardsByIdAccountUseCase.cards) {
        transitionTo { state, payload -> state.copy(listCards = payload) }
      }


      onEach(fetchDepositsUseCase.depositState) {
        transitionTo { state, lceState -> state.copy(depositsLceState = lceState) }
      }

      onEach(fetchDepositsUseCase.deposits) {
        transitionTo { state, depositList -> state.copy(loadedDeposits = depositList) }
      }

      onEach(intent(ProductsHomeIntents::loadDeposits)) {
        action { _, _, _ -> executeAsync { fetchDepositsUseCase() } }
      }

      onEach(fetchDepositsUseCase.terms) {
        transitionTo { state, terms -> state.copy(terms = terms) }
      }

      onEach(intent(ProductsHomeIntents::navigateOnBack)) {
        action { _, _, _ -> flowEvents.tryEmit(FlowEvent.UserLoggedOut) }
      }

      onEach(intent(ProductsHomeIntents::showCardDetails)) {
        action { _, _, cardId ->
          flowEvents.tryEmit(FlowEvent.CardDetails(cardId))
        }
      }
    }
  }
}
