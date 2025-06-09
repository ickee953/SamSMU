/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.samsmu.app.databinding.FragmentUsersListBinding

class UsersListFragment : Fragment() {

    companion object {

        fun getInstance( usersListAdapter: RecyclerView.Adapter<*>): Fragment{
            val fragment = UsersListFragment()
            fragment.usersListAdapter = usersListAdapter

            return fragment
        }
    }

    private var _binding: FragmentUsersListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var usersListAdapter : RecyclerView.Adapter<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if( usersListAdapter != null ) binding.recyclerUserListView.adapter = usersListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}