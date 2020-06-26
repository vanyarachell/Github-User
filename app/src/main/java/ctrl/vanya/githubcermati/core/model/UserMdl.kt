package ctrl.vanya.githubcermati.core.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserMdl (
    @SerializedName("total_count")
    @Expose
    var totalCount: Int? = null,

    @SerializedName("incomplete_results")
    @Expose
    var incompleteResults: Boolean? = null,

    @SerializedName("items")
    @Expose
    var items: List<User>? = null
)