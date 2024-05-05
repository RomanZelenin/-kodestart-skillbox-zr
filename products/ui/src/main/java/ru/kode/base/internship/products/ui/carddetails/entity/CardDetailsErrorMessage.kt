package ru.kode.base.internship.products.ui.carddetails.entity

sealed class CardDetailsErrorMessage {
  sealed class ValidationError : CardDetailsErrorMessage() {
    data object EmptyCardName : ValidationError()
  }

  data object SyncCardName : CardDetailsErrorMessage()
}