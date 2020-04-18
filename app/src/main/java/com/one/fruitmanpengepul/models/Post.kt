package com.one.fruitmanpengepul.models

import com.google.gson.annotations.SerializedName

data class Post (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null
)