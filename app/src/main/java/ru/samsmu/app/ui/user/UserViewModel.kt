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
import ru.samsmu.app.data.db.SamSmuDB
import ru.samsmu.app.data.db.UserDao
import ru.samsmu.app.data.model.User
import ru.samsmu.app.data.model.UsersList

class UserViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val DEFAULT_ERR_MSG = "Error Occured!"
    }

    private val repository: UserRepository = UserRepository( ApiHelper( RetrofitBuilder.apiService ) )
    private val userDao: UserDao = SamSmuDB.getDatabase( application ).userDao()

    fun getUsers() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val usersListResponse: UsersList = repository.getUsersList()
            val users: List<User>? = usersListResponse.users
            emit(Resource.success(data = users))
        } catch( e: Exception ) {
            emit(Resource.error(data = null, message = e.message ?: DEFAULT_ERR_MSG))
        }
    }

    fun getFavorites() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val favorites = userDao.favorites()
        emit(Resource.success(data = favorites))
    }

    fun addFavourite( user: User ) = liveData (Dispatchers.IO) {
        userDao.create(user)
        emit(Resource.success(data = user))
    }

    fun removeFavourite( user: User ) = liveData (Dispatchers.IO) {
        userDao.delete(user)
        emit(Resource.success(data = user))
    }
}