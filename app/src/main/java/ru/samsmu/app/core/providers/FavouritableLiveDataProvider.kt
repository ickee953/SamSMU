/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 26 Jun 2025
 */

package ru.samsmu.app.core.providers

import androidx.lifecycle.LiveData
import ru.samsmu.app.data.Resource

interface FavouritableLiveDataProvider<T> {

    fun addFavourite( itemObject: T) : LiveData<Resource<T>>

    fun removeFavourite( itemObject: T) : LiveData<Resource<T>>

}