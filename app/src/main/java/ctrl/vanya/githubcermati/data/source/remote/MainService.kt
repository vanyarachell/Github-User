package ctrl.vanya.githubcermati.data.source.remote

import ctrl.vanya.githubcermati.core.model.User
import ctrl.vanya.githubcermati.core.model.UserMdl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {
    @GET("search/users")
    suspend fun getListOfUser(@Query("q") mKeyword: String,
                              @Query("page") mPage: Int): Response<UserMdl>
}