/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import ru.samsmu.app.data.Resource
import ru.samsmu.app.data.repository.UserRepository
import ru.samsmu.app.data.api.ApiHelper
import ru.samsmu.app.data.api.RetrofitBuilder
import ru.samsmu.app.data.model.User

class UserViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val DEFAULT_ERR_MSG = "Error Occured!"
    }

    private val repository: UserRepository = UserRepository( ApiHelper( RetrofitBuilder.apiService ) )

    fun getUsers() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        var items: List<User> = ArrayList()
        try {
            items = repository.getUsers()
            emit(Resource.success(data = items))
        } catch( e: Exception ) {
            emit(Resource.error(data = items, message = e.message ?: DEFAULT_ERR_MSG))
        }
    }

    fun getFavorites() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
    }
}