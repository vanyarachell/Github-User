package ctrl.vanya.githubcermati.data.di

import ctrl.vanya.githubcermati.data.source.MainDataSource
import ctrl.vanya.githubcermati.data.source.remote.ApiClient
import ctrl.vanya.githubcermati.data.source.remote.MainService
import ctrl.vanya.githubcermati.data.source.remote.RemoteMainDataSource
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    // Service
    @Provides
    fun provideMainService(): MainService {
        return ApiClient.retrofitClient(
            "https://api.github.com/").create(MainService::class.java)
    }

    // Remote Data Source
    @Provides
    fun provideMainDataSource(mainService: MainService): MainDataSource {
        return RemoteMainDataSource(mainService)
    }
}