package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUsersBinding
import ru.samsmu.app.R
import ru.samsmu.app.ui.Fetchable
import ru.samsmu.app.ui.ReloadableAdapter

class UsersFragment : Fragment(), Fetchable {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private lateinit var usersListFragment: UsersListFragment

    private lateinit var usersListAdapter : UsersListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        //todo fragment live cycle, adaptor dataset save/restore
        /*usersListAdapter = UsersListAdapter(
            requireActivity().application
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

        usersListFragment = UsersListFragment.getInstance(
            this,
            usersListAdapter
        )

        if(savedInstanceState == null){

            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.users_list_fragment, usersListFragment)
            }

        }*/
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun fetch() {

        userViewModel.getUsers().observe(viewLifecycleOwner){ resource ->
            resource.let {
                when(it.status) {
                    Status.SUCCESS -> {
                        //todo hide progress bar
                        it.data.let { data ->

                            usersListAdapter = UsersListAdapter(
                                requireActivity().application
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

                            usersListFragment = UsersListFragment.getInstance(
                                usersListAdapter
                            )

                            requireActivity().supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                addToBackStack(null)
                                add(R.id.users_list_fragment, usersListFragment)
                            }

                            usersListAdapter.reload(data!!)
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