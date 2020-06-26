package ctrl.vanya.githubcermati.feature.di

import ctrl.vanya.githubcermati.feature.ui.MainActivity
import ctrl.vanya.githubcermati.core.di.CoreModule
import ctrl.vanya.githubcermati.data.di.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        MainModule::class,
        MainViewModelModule::class,
        CoreModule::class,
        DataModule::class
    ]
)
interface MainComponent {
    fun inject(activity: MainActivity)
}