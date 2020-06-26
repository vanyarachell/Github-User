package ctrl.vanya.githubcermati.data.utils

import android.annotation.SuppressLint
import com.google.gson.Gson
import ctrl.vanya.githubcermati.core.model.ErrorMdl
import retrofit2.Response

@SuppressLint("DefaultLocale")
fun Throwable.handleError(): ErrorMdl {
    var message: String? = this.message
    if (message != null) {
        if (message.toLowerCase().contains("unable")) {
            message = "Please check your connection"
            return ErrorMdl(message = message, status = false)
        } else if (message.toLowerCase().contains("connection abort")) {
            message = "Please check your connection"
        }
    }

    return ErrorMdl(message = message, status = false)
}

fun Response<*>.handleError(): ErrorMdl {
    val message: String? = this.message()
    var errorParser = ErrorMdl(message = message, status = false)
    val body = this.errorBody()
    if (body != null) {
        val gson = Gson()
        val adapter = gson.getAdapter<ErrorMdl>(ErrorMdl::class.java)

        errorParser = adapter.fromJson(body.string())
    }


    return errorParser
}