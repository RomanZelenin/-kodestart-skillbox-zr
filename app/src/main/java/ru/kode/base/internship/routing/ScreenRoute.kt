package ru.kode.base.internship.routing

sealed class ScreenRoute(val path: String) {
  data object EnterPassword : ScreenRoute(path = "enter-password")
  data object UserIdentification : ScreenRoute(path = "user-identification")
  data object FeatureInProgress : ScreenRoute(path = "feature-in-progress")
  data object ProductsHome: ScreenRoute(path = "products-home")
}
