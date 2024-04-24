package ru.kode.base.internship.products.ui.home

import kotlinx.coroutines.flow.MutableSharedFlow
import ru.dimsuz.unicorn2.Machine
import ru.dimsuz.unicorn2.machine
import ru.kode.base.core.BaseViewModel
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.routing.FlowEvent
import javax.inject.Inject

class ProductsHomeViewModel @Inject constructor(
  private val flowEvents: MutableSharedFlow<FlowEvent>,
) : BaseViewModel<ProductsHomeViewState, ProductsHomeIntents>() {
  override fun buildMachine(): Machine<ProductsHomeViewState> {
    return machine {
      initial = ProductsHomeViewState() to { ProductsHomeMocks.fetchMockData() }

      onEach(intent(ProductsHomeIntents::refresh)) {
        transitionTo { state, _ ->
          state.copy(isRefreshing = true, depositsLceState = LceState.Loading, accountsLceState = LceState.Loading)
        }
        action { _, _, _ -> ProductsHomeMocks.fetchMockData() }
      }

      onEach(ProductsHomeMocks.isRefreshing) {
        transitionTo { state, isRefreshing ->
          state.copy(isRefreshing = isRefreshing)
        }
      }

      onEach(ProductsHomeMocks.accountState) {
        transitionTo { state, lceState ->
          state.copy(accountsLceState = lceState)
        }
      }

      onEach(ProductsHomeMocks.accounts) {
        transitionTo { state, accountList ->
          state.copy(loadedAccounts = accountList)
        }
      }

      onEach(ProductsHomeMocks.depositState) {
        transitionTo { state, lceState ->
          state.copy(depositsLceState = lceState)
        }
      }

      onEach(ProductsHomeMocks.deposits) {
        transitionTo { state, depositList ->
          state.copy(loadedDeposits = depositList)
        }
      }

      onEach(intent(ProductsHomeIntents::loadDeposits)) {
        transitionTo { state, _ ->
          state.copy(depositsLceState = LceState.Loading)
        }
        action { _, _, _ -> ProductsHomeMocks.fetchDeposits() }
      }

      onEach(intent(ProductsHomeIntents::loadAccounts)) {
        transitionTo { state, _ ->
          state.copy(accountsLceState = LceState.Loading)
        }
        action { _, _, _ -> ProductsHomeMocks.fetchAccounts() }
      }

      onEach(intent(ProductsHomeIntents::expandAccount)) {
        transitionTo { state, accountId ->
          if (state.listExpandedAccounts.contains(accountId)) {
            state.copy(listExpandedAccounts = state.listExpandedAccounts.filter { it != accountId })
          } else {
            state.copy(listExpandedAccounts = ArrayList(state.listExpandedAccounts).apply { add(accountId) })
          }
        }
      }

      onEach(intent(ProductsHomeIntents::navigateOnBack)) {
        action { _, _, _ -> flowEvents.tryEmit(FlowEvent.UserLoggedOut) }
      }
    }
  }
}
