package ctrl.vanya.githubcermati.feature.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ctrl.vanya.githubcermati.R
import ctrl.vanya.githubcermati.core.model.User
import ctrl.vanya.githubcermati.core.utils.OnLoadMoreListener
import java.util.*

class MainAdapter(private val context: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listUser = ArrayList<User>()

    private val typeProgress = 0
    private val typeLinear = 1

    var isLoading = false
    private var isMoreDataAvailable = true

    private var loadMoreListener: OnLoadMoreListener? = null

    fun setData(items: List<User>) {
        if (listUser.isNotEmpty()) {
            this.notifyItemInserted(listUser.size - 1)
            if (items.isNotEmpty()) {
                listUser.removeAt(listUser.size - 1)
                listUser.addAll(items)
            } else {
                this.setMoreDataAvailable(false)
            }
        } else {
            listUser.addAll(items)
        }
        notifyDataSetChanged()
        isLoading = false
    }

    fun clearList() {
        listUser.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == typeLinear) {
            UserViewHolder(inflater.inflate(R.layout.item_user, parent, false))
        } else {
            LoadHolder(inflater.inflate(R.layout.item_load_more, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position >= itemCount - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true
            loadMoreListener!!.onLoadMore()
        }

        if (getItemViewType(position) == typeLinear) {
            (holder as UserViewHolder)
            holder.bind(listUser[position], context)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) {
            typeProgress
        } else {
            typeLinear
        }
    }

    override fun getItemCount() = listUser.size

    class UserViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imgPhoto: ImageView = itemView.findViewById(R.id.civ_image)
        private val tvName: TextView = itemView.findViewById(R.id.tv_username)


        fun bind(user: User, context: Activity) {
            if (user.login != null) {
                tvName.text = user.login
                Glide.with(context)
                    .load(user.avatar_url)
                    .into(imgPhoto)

            }
        }
    }

    private class LoadHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    fun setMoreDataAvailable(moreDataAvailable: Boolean) {
        isMoreDataAvailable = moreDataAvailable
    }

    fun setLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }
}