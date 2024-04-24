package ru.kode.base.internship

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.kode.base.core.viewmodel.LocalViewModelStore
import ru.kode.base.internship.di.AppComponent
import ru.kode.base.internship.di.AppComponentHolder
import ru.kode.base.internship.di.ForegroundComponent
import ru.kode.base.internship.routing.AppFlow
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val appComponent = (applicationContext!! as AppComponentHolder).appComponent
    configureEdgeToEdge()
    setContent {
      val navController = rememberNavController()
      val foregroundComponent = rememberForegroundComponent(appComponent, navController)
      val viewModelStore = remember(foregroundComponent) { foregroundComponent.viewModelStore() }

      AppTheme {
        WindowBackgroundEffect(color = AppTheme.colors.backgroundPrimary)
        CompositionLocalProvider(LocalViewModelStore provides viewModelStore) {
          NavHost(navController = navController, startDestination = "root") {
            composable("root") { }
            AppFlow.graph(this)
          }
          AppFlowStartEffect(foregroundComponent)
        }
      }
    }
  }

  @Composable
  internal fun AppFlowStartEffect(foregroundComponent: ForegroundComponent) {
    LaunchedEffect(foregroundComponent) {
      foregroundComponent.appFlowComponent().coordinator().start(
        onFlowFinish = { finish() },
        onError = { }
      )
    }
  }

  @Composable
  private fun WindowBackgroundEffect(color: Color) {
    LaunchedEffect(color) {
      window.setBackgroundDrawable(ColorDrawable(color.toArgb()))
    }
  }

  @Composable
  internal fun rememberForegroundComponent(
    appComponent: AppComponent,
    navController: NavHostController,
  ): ForegroundComponent {
    return remember {
      appComponent.foregroundComponentFactory()
        .create(activity = this, navController = navController)
    }
  }

  private fun configureEdgeToEdge() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.attributes.layoutInDisplayCutoutMode =
      WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
  }
}
