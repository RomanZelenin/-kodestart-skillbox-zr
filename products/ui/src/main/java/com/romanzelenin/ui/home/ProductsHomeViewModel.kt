package com.romanzelenin.ui.home

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
        transitionTo { state, payload ->
          state.copy(loadedAccounts = payload)
        }
      }

      onEach(ProductsHomeMocks.depositState) {
        transitionTo { state, lceState ->
          state.copy(depositsLceState = lceState)
        }
      }

      onEach(ProductsHomeMocks.deposits) {
        transitionTo { state, payload ->
          state.copy(loadedDeposits = payload)
        }
      }

      onEach(intent(ProductsHomeIntents::loadDeposits)) {
        transitionTo { state, payload ->
          state.copy(depositsLceState = LceState.Loading)
        }
        action { state, newState, payload -> ProductsHomeMocks.fetchDeposits() }
      }

      onEach(intent(ProductsHomeIntents::loadAccounts)) {
        transitionTo { state, payload ->
          state.copy(accountsLceState = LceState.Loading)
        }
        action { state, newState, payload -> ProductsHomeMocks.fetchAccounts() }
      }

      onEach(intent(ProductsHomeIntents::navigateOnBack)) {
        action { _, _, _ -> flowEvents.tryEmit(FlowEvent.UserLoggedOut) }
      }
    }
  }
}
