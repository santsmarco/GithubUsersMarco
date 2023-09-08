package com.app.githubusersmarco.model

import android.os.Parcel
import android.os.Parcelable

data class GitHubUser(
    val login: String,
    val id: Int,
    val avatar_url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeInt(id)
        parcel.writeString(avatar_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GitHubUser> {
        override fun createFromParcel(parcel: Parcel): GitHubUser {
            return GitHubUser(parcel)
        }

        override fun newArray(size: Int): Array<GitHubUser?> {
            return arrayOfNulls(size)
        }
    }
}







