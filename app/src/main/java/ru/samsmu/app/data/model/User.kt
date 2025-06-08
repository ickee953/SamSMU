/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id: Long,
    @SerializedName("firstName") var firstName: String?,
    @SerializedName("lastName") var lastName: String?,
    @SerializedName("maidenName") var maidenName: String?,
    @SerializedName("email") var email: String?,
    @SerializedName("image") var image: String?
)
