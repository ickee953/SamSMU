package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUsersBinding
import ru.samsmu.app.R

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onResume(){
        super.onResume()

        loadUsers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadUsers(){

        //todo show progress bar
        userViewModel.getUsers().observe(viewLifecycleOwner){ resource ->
            resource.let {
                when(it.status) {
                    Status.SUCCESS -> {
                        //todo hide progress bar
                        it.data.let { data ->

                            val usersListAdapter = UsersListAdapter(
                                requireActivity().application,
                                data!! as ArrayList<User>
                            ) { itemView ->
                                val user = itemView.tag as User
                                val bundle = Bundle()
                                bundle.putParcelable(
                                    UserDetailsFragment.ARG_USER,
                                    user
                                )
                                itemView.findNavController()
                                    .navigate(R.id.show_user_details, bundle)
                            }

                            val usersListFragment = UsersListFragment.getInstance(usersListAdapter)

                            val transaction: FragmentTransaction
                                = requireActivity().supportFragmentManager.beginTransaction()

                            transaction.replace(R.id.users_list_fragment, usersListFragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
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