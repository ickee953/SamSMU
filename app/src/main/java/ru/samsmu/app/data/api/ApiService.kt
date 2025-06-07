/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.data.api

import retrofit2.http.GET
import ru.samsmu.app.data.model.User

interface ApiService {

    @GET("/users")
    suspend fun getUsers(): List<User>

}