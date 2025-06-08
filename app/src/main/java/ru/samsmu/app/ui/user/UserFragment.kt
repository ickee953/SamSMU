/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUsersListBinding
import java.util.LinkedList

class UserFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userListAdapter : UserListAdapter

    private var users: MutableList<User> = LinkedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userListAdapter = UserListAdapter(users)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerUserListView
        recyclerView.adapter = userListAdapter

        return root
    }

    override fun onResume() {
        super.onResume()

        loadUsers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadUsers(){

        val userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        //todo show progress bar
        userViewModel.getUsers().observe(viewLifecycleOwner){ resource ->
            resource.let {
                when(it.status) {
                    Status.SUCCESS -> {
                        //todo hide progress bar
                        it.data.let { data ->
                            userListAdapter.reloadUsers(data!!)
                        }
                    }

                    Status.ERROR -> {
                        //todo hide progress bar
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                    }

                    Status.LOADING -> {
                        //todo show progress bar
                    }
                }
            }
        }
    }
}