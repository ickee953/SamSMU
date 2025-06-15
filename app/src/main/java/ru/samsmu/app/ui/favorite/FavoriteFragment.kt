package ru.samsmu.app.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.samsmu.app.ui.user.UserViewModel
import androidx.lifecycle.ViewModelProvider
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.ui.user.UserDetailsFragment
import ru.samsmu.app.ui.user.UsersListFragment
import androidx.navigation.findNavController
import ru.samsmu.app.databinding.FragmentFavoriteBinding
import ru.samsmu.app.R
import ru.samsmu.app.ui.Fetchable

class FavoriteFragment : Fragment(), Fetchable {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private lateinit var listFragment: UsersListFragment

    private lateinit var listAdapter : FavoritesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        //todo fragment live cycle, adaptor dataset save/restore
        /*listAdapter = FavoritesListAdapter { itemView ->
            val user = itemView.tag as User
            val bundle = Bundle()
            bundle.putParcelable(
                UserDetailsFragment.ARG_USER,
                user
            )
            itemView.findNavController()
                .navigate(R.id.show_user_details, bundle)
        }

        listFragment = UsersListFragment.getInstance(
            this,
            listAdapter
        )

        if(savedInstanceState == null) {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.favourite_list_fragment, listFragment)
            }
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        fetch()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun fetch(){
        userViewModel.getFavorites().observe(viewLifecycleOwner){ resource ->
            resource.let {
                when(it.status) {
                    Status.SUCCESS -> {
                        //todo hide progress bar
                        it.data.let { data ->
                            listAdapter = FavoritesListAdapter { itemView ->
                                val user = itemView.tag as User
                                val bundle = Bundle()
                                bundle.putParcelable(
                                    UserDetailsFragment.ARG_USER,
                                    user
                                )
                                itemView.findNavController()
                                    .navigate(R.id.show_user_details, bundle)
                            }

                            listFragment = UsersListFragment.getInstance(
                                listAdapter
                            )

                            requireActivity().supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                addToBackStack(null)
                                add(R.id.favourite_list_fragment, listFragment)
                            }

                           listAdapter.reload(data!!)
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