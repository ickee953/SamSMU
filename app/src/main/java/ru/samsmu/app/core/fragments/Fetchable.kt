/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 27 Jun 2025
 */

package ru.samsmu.app.core.fragments

interface Fetchable<T> {
    fun fetch(
        success: (List<T>) -> Unit,
        error: (String?) -> Unit = {},
        loading: () -> Unit = {}
    )
}