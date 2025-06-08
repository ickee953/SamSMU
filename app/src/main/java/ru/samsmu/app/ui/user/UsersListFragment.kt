/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 8 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.samsmu.app.data.model.User

class UsersListFragment : Fragment() {

    private lateinit var usersAdapter: UserListAdapter

    companion object {
        const val ARG_USERS = "users"

        fun getInstance( users: ArrayList<User>? ): Fragment{
            val bundle = Bundle()
            bundle.putParcelableArrayList( ARG_USERS, users )
            val fragment = UsersListFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

}