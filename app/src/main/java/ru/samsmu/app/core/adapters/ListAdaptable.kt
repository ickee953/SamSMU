/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 27 Jun 2025
 */

package ru.samsmu.app.core.adapters

interface ListAdaptable<T> {

    fun setDataset( dataset : Collection<T> )

    fun reloadDataset(  dataset : Collection<T>  )

    fun updateDataset(  dataset : Collection<T>  )

}