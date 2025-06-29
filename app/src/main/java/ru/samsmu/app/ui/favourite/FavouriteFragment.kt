/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.ui.favourite

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import ru.samsmu.app.ui.user.UserViewModel
import androidx.lifecycle.ViewModelProvider
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.ui.user.UserDetailsFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.samsmu.app.databinding.FragmentFavouriteBinding
import ru.samsmu.app.R
import ru.samsmu.app.core.adapters.ActionListAdapter
import ru.samsmu.app.core.fragments.ActionListFragment
import ru.samsmu.app.core.providers.FavourableCallbackProviderImpl
import ru.samsmu.app.core.providers.FavouritableLiveDataProvider
import ru.samsmu.app.core.showConfirmDialog

class FavouriteFragment : ActionListFragment<User, ActionListAdapter<User>>(){

    private var _binding: FragmentFavouriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //todo move actionModeCallback to single class file
        actionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
                mode.menuInflater.inflate(R.menu.favourite_list_action_menu, menu)
                actionMenu = menu
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.option_delete -> {

                        showConfirmDialog(
                            requireContext(),
                            R.string.delete_dialog_title,
                            R.string.deletion_message
                        ) {
                            tracker?.selection?.forEach { userId ->
                                val user = listAdapter.getDataset().find { it.id == userId }
                                if (user != null) {

                                    if(viewModel is FavouritableLiveDataProvider<*>){
                                        //todo unchecked cast
                                        val favouriteCallbackProvider
                                            = FavourableCallbackProviderImpl(
                                                this@FavouriteFragment,
                                                viewModel as FavouritableLiveDataProvider<User>
                                        )

                                        favouriteCallbackProvider.removeFromFavourites(user){
                                            removeListItem(user)
                                        }
                                    }
                                }
                            }
                            mode.finish()
                        }

                        true
                    }

                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                actionMode = null
                tracker?.clearSelection()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListView.adapter = listAdapter

        //requireActivity().addMenuProvider(mainMenuProvider)

        return root
    }

    override fun applySearchFilter() {
        filter(binding.searchEditText.text.toString())
    }

    override fun getRecyclerView(): RecyclerView {
        return binding.recyclerListView
    }

    override fun createListAdapter(): ActionListAdapter<User> {
        return FavouritesListAdapter(R.layout.user_list_item) { itemView ->
            val user = itemView.tag as User
            val bundle = Bundle()
            bundle.putParcelable(
                UserDetailsFragment.ARG_USER,
                user
            )
            itemView.findNavController()
                .navigate(R.id.show_user_details, bundle)
        }
    }

    override fun createViewModel(): AndroidViewModel {
        return ViewModelProvider(this)[UserViewModel::class.java]
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
        //requireActivity().removeMenuProvider(mainMenuProvider)

        super.onDestroyView()
        _binding = null
    }

    override fun fetch(
        success: (List<User>) -> Unit,
        error: (String?) -> Unit,
        loading: () -> Unit
    ) {
        (viewModel as UserViewModel).getFavorites().observe(viewLifecycleOwner){ resource ->
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