package ctrl.vanya.githubcermati.data.source.remote

import ctrl.vanya.githubcermati.core.model.UserMdl
import ctrl.vanya.githubcermati.data.source.MainDataSource
import ctrl.vanya.githubcermati.data.utils.ResultState
import ctrl.vanya.githubcermati.data.utils.fetchState
import ctrl.vanya.githubcermati.data.utils.handleError
import javax.inject.Inject

class RemoteMainDataSource @Inject constructor(
    private val mainService: MainService
) : MainDataSource {
    override suspend fun getUser(mKeyword: String, mPage: Int): ResultState<UserMdl> {
        return fetchState {
            val response = mainService.getListOfUser(mKeyword, mPage)
            val data: UserMdl
            if (response.isSuccessful) {
                data = response.body()!!
                ResultState.Success(data)
            } else {
                ResultState.Error(response.handleError().message)
            }
        }
    }
}