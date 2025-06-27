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
import ru.samsmu.app.core.providers.FavouritableLiveDataProvider

class UserViewModel(application: Application) : AndroidViewModel(application),
    FavouritableLiveDataProvider<User> {

    companion object {
        const val DEFAULT_ERR_MSG = "Error Occured!"
    }

    private suspend fun withFavourite(users : List<User>) : List<User>{
        val favourites = userDao.favorites()

        users.forEach { user ->
            if( favourites.contains(user) ) user.isFavourite = 1
        }

        return users
    }

    private val repository: UserRepository = UserRepository( ApiHelper( RetrofitBuilder.apiService ) )
    private val userDao: UserDao = SamSmuDB.getDatabase( application ).userDao()

    fun getUsers() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val usersListResponse: UsersList = repository.getUsersList()
            val users = usersListResponse.users?.let { withFavourite(it) }
            if( users != null ){
                emit(Resource.success(data = users))
            } else {
                emit(Resource.error(data = null, message = "Received list is null"))
            }
        } catch( e: Exception ) {
            emit(Resource.error(data = null, message = e.message ?: DEFAULT_ERR_MSG))
        }
    }

    fun getFavorites() = liveData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val favorites = userDao.favorites()
        favorites.forEach { user->
            user.isFavourite = 1
        }
        emit(Resource.success(data = favorites))
    }

    override fun addFavourite(itemObject: User ) = liveData (Dispatchers.IO) {
        userDao.create(itemObject)
        emit(Resource.success(data = itemObject))
    }

    override fun removeFavourite(itemObject: User ) = liveData (Dispatchers.IO) {
        userDao.delete(itemObject)
        emit(Resource.success(data = itemObject))
    }
}