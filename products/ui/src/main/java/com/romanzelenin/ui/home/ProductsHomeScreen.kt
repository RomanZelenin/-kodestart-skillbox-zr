package com.romanzelenin.ui.home

import androidx.compose.runtime.Composable
import ru.kode.base.core.rememberViewIntents
import ru.kode.base.core.viewmodel.daggerViewModel
import ru.kode.base.internship.ui.core.uikit.screen.AppScreen


@Composable
fun ProductsHomeScreen(viewModel: ProductsHomeViewModel = daggerViewModel()) =
  AppScreen(
    viewModel = viewModel,
    intents = rememberViewIntents()
  ) { state, intents ->

  }