/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.core.providers

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.samsmu.app.core.OnCheckedItemListener
import ru.samsmu.app.core.fragments.ActionListFragment

open class FragmentFavouriteCheckedProvider<T>(
    private val fragment: Fragment,
    favouriteLiveData: FavouritableLiveDataProvider<T>
) : FavourableCallbackProviderImpl<T>(fragment, favouriteLiveData), OnCheckedItemListener<T> {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCheckedChanged(itemObject: T?, view: View, isChecked: Boolean) {
        if (itemObject != null) {
            if(isChecked) {
                addToFavourites( itemObject, success = { result ->
                    fragment.setFragmentResult(
                        ActionListFragment.ARG_ITEM_LIST_CHANGED,
                        bundleOf(ActionListFragment.ARG_ITEM to result)
                    )

                    view.isEnabled = true
                }, error = { message ->
                    view.isEnabled = true

                    Toast.makeText(
                        fragment.requireActivity(),
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }, loading = { view.isEnabled = false })
            } else {
                removeFromFavourites( itemObject, success = { result ->
                    fragment.setFragmentResult(
                        ActionListFragment.ARG_ITEM_LIST_CHANGED,
                        bundleOf(ActionListFragment.ARG_ITEM to result)
                    )

                    view.isEnabled = true
                }, error = { message ->
                    view.isEnabled = true

                    Toast.makeText(
                        fragment.requireActivity(),
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }, loading = { view.isEnabled = false } )
            }
        }
    }
}