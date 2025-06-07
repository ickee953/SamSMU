package ru.samsmu.app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Long,
    @SerializedName("firstName") var firstName: String?,
    @SerializedName("lastName") var lastName: String?,
    @SerializedName("middleName") var middleName: String?,
    @SerializedName("email") var email: String?,
    @SerializedName("image") var image: String?
)