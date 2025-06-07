/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.samsmu.app.data.repository.UserRepository
import ru.samsmu.app.data.api.ApiHelper
import ru.samsmu.app.data.api.RetrofitBuilder

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val DEFAULT_ERR_MSG = "Error Occured!"
    }

    private val repository: UserRepository = UserRepository( ApiHelper( RetrofitBuilder.apiService ) )

    /*fun getUsers() = livaData (Dispatchers.IO) {
        emit(Resource.loading(data = null))
        val items = null
        try {
            items = repository.getUsers()
            emit(Resource.success(data = items))
        } catch( e: Exceeption ) {
            emit(Resource.error(data = items, message = e.message ?: DEFAULT_ERR_MSG))
        }
    }*/
}