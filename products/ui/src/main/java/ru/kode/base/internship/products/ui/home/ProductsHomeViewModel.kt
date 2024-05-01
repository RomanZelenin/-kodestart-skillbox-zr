package ru.kode.base.internship.products.ui.home

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.dimsuz.unicorn2.Machine
import ru.dimsuz.unicorn2.machine
import ru.kode.base.core.BaseViewModel
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.products.domain.usecase.FetchAccountsUseCase
import ru.kode.base.internship.products.domain.usecase.FetchDepositsUseCase
import ru.kode.base.internship.routing.FlowEvent
import javax.inject.Inject

class ProductsHomeViewModel @Inject constructor(
  private val flowEvents: MutableSharedFlow<FlowEvent>,
  private val fetchAccountsUseCase: FetchAccountsUseCase,
  private val fetchDepositsUseCase: FetchDepositsUseCase,
) : BaseViewModel<ProductsHomeViewState, ProductsHomeIntents>() {

  override fun buildMachine(): Machine<ProductsHomeViewState> {
    return machine {
      initial = ProductsHomeViewState() to {
        executeAsync {
          launch { fetchAccountsUseCase() }
          launch { fetchDepositsUseCase() }
        }
      }


      onEach(intent(ProductsHomeIntents::refresh)) {
        transitionTo { state, _ -> state.copy(isRefreshing = true) }
        action { _, _, _ ->
          executeAsync {
            launch { fetchAccountsUseCase() }
            launch { fetchDepositsUseCase() }
          }
        }
      }

      onEach(fetchAccountsUseCase.accountState) {
        transitionTo { state, lceState ->
          state.copy(
            accountsLceState = lceState,
            expandedAccountIds = emptyList(),
            isRefreshing = (lceState is LceState.Loading) || (state.depositsLceState is LceState.Loading)
          )
        }
      }

      onEach(fetchAccountsUseCase.accounts) {
        transitionTo { state, accountList ->
          state.copy(accounts = accountList)
        }
      }

      onEach(intent(ProductsHomeIntents::loadAccounts)) {
        action { _, _, _ -> executeAsync { fetchAccountsUseCase() } }
      }

      onEach(intent(ProductsHomeIntents::expandAccount)) {
        transitionTo { state, id ->
          if (state.expandedAccountIds.contains(id)) {
            state.copy(expandedAccountIds = state.expandedAccountIds.filter { it != id })
          } else {
            state.copy(expandedAccountIds = ArrayList(state.expandedAccountIds).apply { add(id) })
          }
        }
      }

      onEach(fetchDepositsUseCase.depositState) {
        transitionTo { state, lceState ->
          state.copy(
            depositsLceState = lceState,
            isRefreshing = (lceState is LceState.Loading) || (state.accountsLceState is LceState.Loading)
          )
        }
      }

      onEach(fetchDepositsUseCase.deposits) {
        transitionTo { state, depositList -> state.copy(deposits = depositList) }
      }

      onEach(intent(ProductsHomeIntents::loadDeposits)) {
        action { _, _, _ -> executeAsync { fetchDepositsUseCase() } }
      }

      onEach(intent(ProductsHomeIntents::navigateOnBack)) {
        action { _, _, _ -> flowEvents.tryEmit(FlowEvent.UserLoggedOut) }
      }

      onEach(intent(ProductsHomeIntents::showCardDetails)) {
        action { _, _, ids ->
          flowEvents.tryEmit(FlowEvent.CardDetails(accountId = ids.accountId, cardId = ids.cardId))
        }
      }
    }
  }
}
