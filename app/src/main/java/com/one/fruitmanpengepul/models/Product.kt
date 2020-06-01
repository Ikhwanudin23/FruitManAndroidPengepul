package com.one.fruitmanpengepul.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("address") var address : String? = null,
    @SerializedName("image") var image : String? = null,
    @SerializedName("price")var price : String? = null,
    @SerializedName("user") var user : User? = null
) : Parcelable
