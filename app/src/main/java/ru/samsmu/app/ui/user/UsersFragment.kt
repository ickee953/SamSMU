package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUsersBinding
import ru.samsmu.app.R
import ru.samsmu.app.ui.Fetchable
import ru.samsmu.app.ui.OnCheckedItemListener
import ru.samsmu.app.ui.favorite.FavoriteFragment

class UsersFragment : Fragment(), Fetchable {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private lateinit var usersListAdapter : UsersListAdapter

    private var list : MutableList<User> = ArrayList()

    companion object {
        const val ARG_LIST = "users_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        usersListAdapter = UsersListAdapter({ itemView ->
            val user = itemView.tag as User
            val bundle = Bundle()
            bundle.putParcelable(
                UserDetailsFragment.ARG_USER,
                user
            )
            itemView.findNavController()
                .navigate(R.id.show_user_details, bundle)
        }, object : OnCheckedItemListener<User> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onCheckedChanged(itemObject: User?, view: View, isChecked: Boolean) {
                if (itemObject != null) {
                    if(isChecked) {
                        userViewModel.addFavourite(itemObject).observe(viewLifecycleOwner){ res->
                            res?.let {
                                when(it.status){
                                    Status.SUCCESS -> {
                                        list.forEach { item ->
                                            if(item == itemObject){

                                                item.isFavourite = 1

                                                setFragmentResult(
                                                    FavoriteFragment.ARG_FAVOURITE_LIST_CHANGED,
                                                    Bundle()
                                                )
                                            }
                                        }

                                        view.isEnabled = true
                                    }
                                    Status.ERROR -> {
                                        view.isEnabled = true

                                        Toast.makeText(
                                            requireActivity(),
                                            it.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    Status.LOADING -> {
                                        view.isEnabled = false
                                    }
                                }
                            }
                        }
                    } else {
                        userViewModel.removeFavourite(itemObject).observe(viewLifecycleOwner){ res->
                            res?.let {
                                when(it.status){
                                    Status.SUCCESS -> {
                                        list.forEach { item ->
                                            if(item == itemObject){

                                                item.isFavourite = 0

                                                setFragmentResult(
                                                    FavoriteFragment.ARG_FAVOURITE_LIST_CHANGED,
                                                    Bundle()
                                                )
                                            }
                                        }

                                        view.isEnabled = true
                                    }
                                    Status.ERROR -> {
                                        view.isEnabled = true

                                        Toast.makeText(
                                            requireActivity(),
                                            it.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    Status.LOADING -> {
                                        view.isEnabled = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            list = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListView.adapter = usersListAdapter

        return root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState == null){
            fetch({ items ->
                list = items as ArrayList
                usersListAdapter.reload(list)
                //todo hide progress bar
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                //todo hide progress bar
            }, {
                //todo show progress bar
            })
        } else if(savedInstanceState.containsKey(ARG_LIST)){
            list = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            usersListAdapter.reload(list)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ARG_LIST, list as ArrayList<User>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun fetch(
        success: (List<User>) -> Unit,
        error: (String?) -> Unit,
        loading: () -> Unit) {

        userViewModel.getUsers().observe(viewLifecycleOwner){ resource ->
            resource.let {
                when(it.status) {
                    Status.SUCCESS -> success(it.data!!)
                    Status.ERROR -> error(it.message)
                    Status.LOADING -> loading()
                }
            }
        }
    }
}