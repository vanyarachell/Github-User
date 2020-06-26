package ctrl.vanya.githubcermati.core.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val login: String?,
    val id: Int,
    val avatar_url: String?,
    val repos_url: String?,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val public_repos: Int,
    val public_gist: Int,
    val followers: Int,
    val following: Int,
    val total_private_repos: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeInt(id)
        parcel.writeString(avatar_url)
        parcel.writeString(repos_url)
        parcel.writeString(name)
        parcel.writeString(company)
        parcel.writeString(blog)
        parcel.writeString(location)
        parcel.writeString(email)
        parcel.writeString(bio)
        parcel.writeInt(public_repos)
        parcel.writeInt(public_gist)
        parcel.writeInt(followers)
        parcel.writeInt(following)
        parcel.writeInt(total_private_repos)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}