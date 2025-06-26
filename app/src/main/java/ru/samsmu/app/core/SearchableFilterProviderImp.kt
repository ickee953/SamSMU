/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 26 Jun 2025
 */

package ru.samsmu.app.core

import java.util.LinkedList
import java.util.Locale

class SearchableFilterProviderImp<T>: SearchableFilterProvider<T> {

    override fun filter(
        list : Collection<T>?,
        text: String?,
        callback: (List<T>) -> Unit
    ){
        if(list != null) {
            val filtered: MutableList<T> = LinkedList()
            for (user in list) {
                if (user.toString().lowercase(Locale.getDefault())
                        .contains(text?.lowercase(Locale.getDefault()).toString())
                ) {
                    filtered.add(user)
                }
            }
            callback(filtered)
        }
    }

}