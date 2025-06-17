package ru.samsmu.app.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import ru.samsmu.app.ui.user.UserViewModel
import androidx.lifecycle.ViewModelProvider
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.ui.user.UserDetailsFragment
import androidx.navigation.findNavController
import ru.samsmu.app.databinding.FragmentFavoriteBinding
import ru.samsmu.app.R
import ru.samsmu.app.ui.Fetchable

class FavoriteFragment : Fragment(), Fetchable {

    companion object {
        const val ARG_LIST                      = "favourites_list"
        const val ARG_FAVOURITE_LIST_CHANGED    = "favourite_list_changed"
    }

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private lateinit var listAdapter : FavoritesListAdapter

    private var list : MutableList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

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

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            list = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
        }

        setFragmentResultListener(ARG_FAVOURITE_LIST_CHANGED) { _, _->
            fetch({ items ->
                list = items as ArrayList
                listAdapter.reload(list)
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListView.adapter = listAdapter

        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            list = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            listAdapter.reload(list)
        } else {
            fetch({ items ->
                list = items as ArrayList
                listAdapter.reload(list)
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            })
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
        loading: () -> Unit
    ) {
        userViewModel.getFavorites().observe(viewLifecycleOwner){ resource ->
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