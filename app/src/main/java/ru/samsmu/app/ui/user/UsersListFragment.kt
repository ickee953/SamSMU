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
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUsersListBinding
import ru.samsmu.app.ui.Fetchable
import ru.samsmu.app.ui.ReloadableAdapter

class UsersListFragment : Fragment() {

    private lateinit var listAdapter : ReloadableAdapter<User>

    //private  lateinit var fetchable : Fetchable

    companion object {

        fun getInstance(
            //fetchable: Fetchable,
            adapter: ReloadableAdapter<User>
        ) : UsersListFragment {

            val fragment = UsersListFragment()

            //fragment.fetchable = fetchable
            fragment.listAdapter = adapter

            return fragment
        }
    }

    private var _binding: FragmentUsersListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        binding.recyclerUserListView.adapter = listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}