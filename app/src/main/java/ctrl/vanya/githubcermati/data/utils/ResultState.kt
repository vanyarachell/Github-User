package ctrl.vanya.githubcermati.data.utils

sealed class ResultState<out T : Any?> {
    data class Success<out T : Any?>(
        val data: T?,
        val isLast: Boolean = false) : ResultState<T>()
    data class Error(val errorMessage: String?) : ResultState<Nothing>()
    data class ErrorWithCode(
        val code: Int, val errorMessage: String?) : ResultState<Nothing>()
}