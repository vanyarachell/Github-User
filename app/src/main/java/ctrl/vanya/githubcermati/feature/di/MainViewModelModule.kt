package ctrl.vanya.githubcermati.feature.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ctrl.vanya.githubcermati.core.base.BaseViewModelFactory
import ctrl.vanya.githubcermati.core.base.ViewModelKey
import ctrl.vanya.githubcermati.feature.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: BaseViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindDriverViewModel(viewModel: MainViewModel): ViewModel
}