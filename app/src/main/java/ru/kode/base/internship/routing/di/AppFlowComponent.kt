package ru.kode.base.internship.routing.di

import com.romanzelenin.ui.home.ProductsHomeViewModel
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.kode.base.core.BaseViewModel
import ru.kode.base.core.di.SingleIn
import ru.kode.base.core.viewmodel.ViewModelKey
import ru.kode.base.internship.routing.AppFlow
import ru.kode.base.internship.routing.FlowEvent
import ru.kode.base.internship.ui.identification.UserIdentificationViewModel
import ru.kode.base.internship.ui.password.EnterPasswordViewModel

@SingleIn(AppFlowScope::class)
@MergeSubcomponent(AppFlowScope::class)
interface AppFlowComponent {
  fun coordinator(): AppFlow.Coordinator
}

@Module
@ContributesTo(AppFlowScope::class)
object AppFlowModule {
  @Provides
  @SingleIn(AppFlowScope::class)
  fun providesFlowEvents(): MutableSharedFlow<FlowEvent> = MutableSharedFlow(extraBufferCapacity = 3)
}

// All used ViewModels should be listed here
@Module
@ContributesTo(AppFlowScope::class)
interface AppFlowUiModule {
  @Binds
  @IntoMap
  @ViewModelKey(UserIdentificationViewModel::class)
  fun bindUserIdentificationViewModel(model: UserIdentificationViewModel): BaseViewModel<*, *>

  @Binds
  @IntoMap
  @ViewModelKey(EnterPasswordViewModel::class)
  fun bindEnterPasswordViewModel(model: EnterPasswordViewModel): BaseViewModel<*, *>

  @Binds
  @IntoMap
  @ViewModelKey(ProductsHomeViewModel::class)
  fun bindProductsHomeViewModel(model: ProductsHomeViewModel): BaseViewModel<*, *>
}
