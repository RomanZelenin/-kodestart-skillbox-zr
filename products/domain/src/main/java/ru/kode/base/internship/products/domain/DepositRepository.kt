package ru.kode.base.internship.products.domain

import kotlinx.coroutines.flow.Flow
import ru.kode.base.internship.products.domain.entity.Deposit

interface DepositRepository {

  val deposits: Flow<List<Deposit>>

  suspend fun fetchDeposits()
}
