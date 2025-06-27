/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.core

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.samsmu.app.core.fragments.ActionListFragment
import ru.samsmu.app.data.Status

open class FragmentFavouriteCheckedProvider<T>(
    private val fragment: Fragment,
    private val favouritableLiveData: FavouritableLiveData<T>
) : OnCheckedItemListener<T>, FavouritesProvider<T> {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCheckedChanged(itemObject: T?, view: View, isChecked: Boolean) {
        if (itemObject != null) {
            if(isChecked) {
                addToFavourites( itemObject, { result ->
                    fragment.setFragmentResult(
                        ActionListFragment.ARG_ITEM_LIST_CHANGED,
                        bundleOf(ActionListFragment.ARG_ITEM to result)
                    )

                    view.isEnabled = true
                },{ message ->
                    view.isEnabled = true

                    Toast.makeText(
                        fragment.requireActivity(),
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                },{ view.isEnabled = false })
            } else {
                removeFromFavourites( itemObject, { result ->
                    fragment.setFragmentResult(
                        ActionListFragment.ARG_ITEM_LIST_CHANGED,
                        bundleOf(ActionListFragment.ARG_ITEM to result)
                    )

                    view.isEnabled = true
                }, { message ->
                    view.isEnabled = true

                    Toast.makeText(
                        fragment.requireActivity(),
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }, { view.isEnabled = false } )
            }
        }
    }

    override fun addToFavourites(
        itemObject: T,
        success: (T) -> Unit,
        error: (String?) -> Unit,
        loading: () -> Unit
    ){
        favouritableLiveData.addFavourite(itemObject).observe(fragment.viewLifecycleOwner){ res->
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
        success: (T) -> Unit,
        error: (String?) -> Unit,
        loading: () -> Unit
    ){
        favouritableLiveData.removeFavourite(itemObject).observe(fragment.viewLifecycleOwner){ res->
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