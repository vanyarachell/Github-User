package ctrl.vanya.githubcermati.feature.di

import ctrl.vanya.githubcermati.data.repository.MainRepository
import ctrl.vanya.githubcermati.data.source.remote.RemoteMainDataSource
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    fun provideMainRepository(
        remoteMainDataSource: RemoteMainDataSource
    ): MainRepository {
        return MainRepository.MainRepositoryImpl(remoteMainDataSource)
    }
}