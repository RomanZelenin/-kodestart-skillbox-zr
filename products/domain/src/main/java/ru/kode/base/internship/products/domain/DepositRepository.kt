package ru.kode.base.internship.products.domain

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.entity.Deposit
import ru.kode.base.internship.products.domain.entity.DepositTerms

interface DepositRepository {
  val deposits: Flow<List<Deposit>>
  fun term(id: Deposit.Id): Flow<DepositTerms>

  suspend fun getDeposits():List<Deposit>

  suspend fun getTerm(id: Deposit.Id):DepositTerms
}
