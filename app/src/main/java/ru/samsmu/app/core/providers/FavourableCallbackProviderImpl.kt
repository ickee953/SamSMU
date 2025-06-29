/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 29 Jun 2025
 */

package ru.samsmu.app.core.providers

import androidx.fragment.app.Fragment
import ru.samsmu.app.data.Status

open class FavourableCallbackProviderImpl<T>(
    private val fragment: Fragment,
    private val favouriteLiveData: FavouritableLiveDataProvider<T>
) : FavourableCallbackProvider<T> {

    override fun addToFavourites(
        itemObject: T,
        error: (String?) -> Unit,
        loading: () -> Unit,
        success: (T) -> Unit
    ){
        favouriteLiveData.addFavourite(itemObject).observe(fragment.viewLifecycleOwner){ res->
            res?.let {
                when(it.status){
                    Status.SUCCESS -> {
                        val ret = it.data
                        if( ret != null ) success(it.data)
                        else error("Returned object is null")
                    }
                    Status.ERROR -> { error(it.message) }
                    Status.LOADING -> { loading() }
                }
            }
        }
    }

    override fun removeFromFavourites(
        itemObject: T,
        error: (String?) -> Unit,
        loading: () -> Unit,
        success: (T) -> Unit
    ){
        favouriteLiveData.removeFavourite(itemObject).observe(fragment.viewLifecycleOwner){ res->
            res?.let {
                when(it.status){
                    Status.SUCCESS -> {
                        val ret = it.data
                        if( ret != null ) success(it.data)
                        else error("Returned object is null")
                    }
                    Status.ERROR -> { error(it.message) }
                    Status.LOADING -> { loading() }
                }
            }
        }
    }

}