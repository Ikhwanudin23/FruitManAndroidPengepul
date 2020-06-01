package com.one.fruitmanpengepul.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("seller") var seller : User,
    @SerializedName("collector") var collector : User,
    @SerializedName("product") var product: Product,
    @SerializedName("offer_price") var offer_price : Int? = 0,
    @SerializedName("status") var status : String? = null
) : Parcelable