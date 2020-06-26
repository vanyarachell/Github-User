package ctrl.vanya.githubcermati.data.source

import ctrl.vanya.githubcermati.core.model.User
import ctrl.vanya.githubcermati.core.model.UserMdl
import ctrl.vanya.githubcermati.data.utils.ResultState

interface MainDataSource {
    suspend fun getUser(mKeyword: String, mPage: Int): ResultState<UserMdl>
}