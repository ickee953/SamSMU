/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUsersBinding
import ru.samsmu.app.R
import ru.samsmu.app.core.FragmentFavouriteCheckedProvider
import ru.samsmu.app.core.ReloadableAdapter
import ru.samsmu.app.core.fragments.ListFragment

class UsersFragment : ListFragment<User, ReloadableAdapter<User>>() {

    private var _binding: FragmentUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListView.adapter = listAdapter

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
                filter(s.toString())
            }
        })
        applySearchFilter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun fetch(
        success: (List<User>) -> Unit,
        error: (String?) -> Unit,
        loading: () -> Unit) {

        (viewModel as UserViewModel).getUsers().observe(viewLifecycleOwner){ resource ->
            resource.let {
                when(it.status) {
                    Status.SUCCESS -> success(it.data!!)
                    Status.ERROR -> error(it.message)
                    Status.LOADING -> loading()
                }
            }
        }
    }

    override fun applySearchFilter(){
        filter(binding.searchEditText.text.toString())
    }

    override fun getRecyclerView(): RecyclerView {
        return binding.recyclerListView
    }

    override fun createListAdapter(): ReloadableAdapter<User> {
        return UsersListAdapter(R.layout.user_list_item, { itemView ->
            val user = itemView.tag as User
            val bundle = Bundle()
            bundle.putParcelable(
                UserDetailsFragment.ARG_USER,
                user
            )
            itemView.findNavController()
                .navigate(R.id.show_user_details, bundle)
        }, object : FragmentFavouriteCheckedProvider<User>(this, viewModel as UserViewModel) {
            override fun onCheckedChanged(itemObject: User?, view: View, isChecked: Boolean) {
                super.onCheckedChanged(itemObject, view, isChecked)
                //update list item element
                list?.let { l ->
                    l.find { it == itemObject }?.let {
                        if (isChecked) it.isFavourite = 1
                        else it.isFavourite = 0
                    }
                }
            }
        })
    }

    override fun createViewModel(): AndroidViewModel {
        return ViewModelProvider(this)[UserViewModel::class.java]
    }
}