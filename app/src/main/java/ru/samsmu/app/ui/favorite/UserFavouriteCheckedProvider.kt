/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.ui.favorite

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.ui.OnCheckedItemListener
import ru.samsmu.app.ui.user.UserViewModel

open class UserFavouriteCheckedProvider(
    private val fragment: Fragment,
    private val viewModel: UserViewModel
) : OnCheckedItemListener<User> {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCheckedChanged(itemObject: User?, view: View, isChecked: Boolean) {
        if (itemObject != null) {
            if(isChecked) {
                addToFavourites( itemObject, { result ->
                    fragment.setFragmentResult(
                        FavoriteFragment.ARG_FAVOURITE_LIST_CHANGED,
                        bundleOf(FavoriteFragment.ARG_USER to result)
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
                        FavoriteFragment.ARG_FAVOURITE_LIST_CHANGED,
                        bundleOf(FavoriteFragment.ARG_USER to result)
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

    fun addToFavourites(
        itemObject: User,
        success: (User) -> Unit,
        error: (String?) -> Unit,
        loading: () -> Unit
    ){
        viewModel.addFavourite(itemObject).observe(fragment.viewLifecycleOwner){ res->
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

    fun removeFromFavourites(
        itemObject: User,
        success: (User) -> Unit,
        error: (String?) -> Unit,
        loading: () -> Unit
    ){
        viewModel.removeFavourite(itemObject).observe(fragment.viewLifecycleOwner){ res->
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