/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 8 Jun 2025
 */

package ru.samsmu.app.data.model

import com.google.gson.annotations.SerializedName

data class UsersList(
    @SerializedName("users") var users: List<User>?
)
