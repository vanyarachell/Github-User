package ctrl.vanya.githubcermati.data.repository

import ctrl.vanya.githubcermati.core.model.UserMdl
import ctrl.vanya.githubcermati.data.source.MainDataSource
import ctrl.vanya.githubcermati.data.utils.ResultState
import javax.inject.Inject

interface MainRepository {
    suspend fun getUser(mKeyword: String, mPage: Int): ResultState<UserMdl>

    class MainRepositoryImpl @Inject constructor(private val remoteMainDataSource: MainDataSource) : MainRepository {
        override suspend fun getUser(mKeyword: String, mPage: Int): ResultState<UserMdl> {
            return remoteMainDataSource.getUser(mKeyword, mPage)
        }

    }
}