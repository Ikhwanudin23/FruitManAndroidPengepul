package com.one.fruitmanpengepul.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("api_token") var token : String? = null,
    @SerializedName("image") var image : String? = null,
    @SerializedName("address") var address : String? = null,
    @SerializedName("phone") var phone : String? = null
) : Parcelable