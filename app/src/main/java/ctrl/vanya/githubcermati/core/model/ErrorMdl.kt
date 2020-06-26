package ctrl.vanya.githubcermati.core.model

data class ErrorMdl (
    val status: Boolean = false,
    val http_code: Int? = null,
    val message: String? = null
)