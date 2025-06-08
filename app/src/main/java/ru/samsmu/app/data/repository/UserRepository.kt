/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.data.repository

import ru.samsmu.app.data.api.ApiHelper

class UserRepository( private val apiHelper: ApiHelper ) {

    suspend fun getUsersList() = apiHelper.getUsersList()

}