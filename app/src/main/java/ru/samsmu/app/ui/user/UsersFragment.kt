/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUsersBinding
import ru.samsmu.app.R
import ru.samsmu.app.core.Fetchable
import ru.samsmu.app.core.SearchableFilterProvider
import ru.samsmu.app.core.SearchableFilterProviderImp
import ru.samsmu.app.core.FragmentFavouriteCheckedProvider

class UsersFragment : Fragment(), Fetchable {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private lateinit var usersListAdapter : UsersListAdapter

    private lateinit var searchFilterProvider : SearchableFilterProvider<User>

    private var usersList : Collection<User>? = null

    companion object {
        const val ARG_LIST = "users_list"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        usersListAdapter = UsersListAdapter(R.layout.user_list_item, { itemView ->
            val user = itemView.tag as User
            val bundle = Bundle()
            bundle.putParcelable(
                UserDetailsFragment.ARG_USER,
                user
            )
            itemView.findNavController()
                .navigate(R.id.show_user_details, bundle)
        }, object : FragmentFavouriteCheckedProvider<User>(this, userViewModel) {
            override fun onCheckedChanged(itemObject: User?, view: View, isChecked: Boolean) {
                super.onCheckedChanged(itemObject, view, isChecked)
                //update list item element
                val list = usersListAdapter.getDataset()
                list.find { it == itemObject }?.let {
                    if(isChecked) it.isFavourite = 1
                    else it.isFavourite = 0
                }
            }
        })

        searchFilterProvider = SearchableFilterProviderImp()

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            usersList = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            usersListAdapter.setDataset(usersList)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.searchEditText.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                applySearchFilter(s.toString())
            }
        })
        applySearchFilter(binding.searchEditText.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            usersList = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            usersListAdapter.reload(usersList)
            applySearchFilter(binding.searchEditText.text.toString())
        } else {
            fetch({ items ->
                usersList =  items
                usersListAdapter.reload(usersList as ArrayList)
                applySearchFilter(binding.searchEditText.text.toString())
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        usersList?.let {
            outState.putParcelableArrayList(ARG_LIST, it as ArrayList<User>)
        }
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

    private fun applySearchFilter(text : String?) {
        searchFilterProvider.filter(usersList, text) { filtered->
            usersListAdapter.update(filtered)
        }
    }
}