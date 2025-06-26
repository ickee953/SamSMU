/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 26 Jun 2025
 */

package ru.samsmu.app.core

interface SearchableFilterProvider<T> {

    fun filter(
        list : Collection<T>?,
        text: String?,
        callback: (List<T>) -> Unit)

}