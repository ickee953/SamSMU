/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 26 Jun 2025
 */

package ru.samsmu.app.core.providers

interface FavourableCallbackProvider<T> {

    fun addToFavourites(
        itemObject: T,
        error: (String?) -> Unit = {},
        loading: () -> Unit = {},
        success: (T) -> Unit
    )

    fun removeFromFavourites(
        itemObject: T,
        error: (String?) -> Unit = {},
        loading: () -> Unit = {},
        success: (T) -> Unit
    )
}