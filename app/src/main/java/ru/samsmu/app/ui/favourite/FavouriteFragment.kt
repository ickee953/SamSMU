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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import ru.samsmu.app.ui.user.UserViewModel
import androidx.lifecycle.ViewModelProvider
import ru.samsmu.app.data.Status
import ru.samsmu.app.data.model.User
import ru.samsmu.app.ui.user.UserDetailsFragment
import androidx.navigation.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import ru.samsmu.app.databinding.FragmentFavouriteBinding
import ru.samsmu.app.R
import ru.samsmu.app.core.Fetchable
import ru.samsmu.app.core.SearchableFilterProvider
import ru.samsmu.app.core.SearchableFilterProviderImp
import ru.samsmu.app.core.showConfirmDialog

class FavouriteFragment : Fragment(), Fetchable {

    companion object {
        const val ARG_LIST                      = "favourites_list"
        const val ARG_USER                      = "favourites_item"
        const val ARG_FAVOURITE_LIST_CHANGED    = "favourite_list_changed"
    }

    private var _binding: FragmentFavouriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private lateinit var searchProvider : SearchableFilterProvider<User>

    private lateinit var listAdapter : FavouritesListAdapter

    private var usersList : Collection<User>? = null

    private var actionMode: ActionMode? = null

    private var tracker: SelectionTracker<Long>? = null

    private var actionMenu: Menu? = null

    private val actModeCallback: ActionMode.Callback = object : ActionMode.Callback {
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
                            val user = listAdapter.getDataset().find { it.id == userId}
                            if (user != null) {
                                userViewModel.removeFavourite(user).observe(viewLifecycleOwner) { res ->
                                    if(res.status == Status.SUCCESS) {
                                        listAdapter.remove(user)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        listAdapter = FavouritesListAdapter(R.layout.user_list_item) { itemView ->
            val user = itemView.tag as User
            val bundle = Bundle()
            bundle.putParcelable(
                UserDetailsFragment.ARG_USER,
                user
            )
            itemView.findNavController()
                .navigate(R.id.show_user_details, bundle)
        }

        searchProvider = SearchableFilterProviderImp()

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            usersList = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            listAdapter.setDataset(usersList)
        }

        setFragmentResultListener(ARG_FAVOURITE_LIST_CHANGED) { _, _->
            fetch({ items ->
                usersList = items
                listAdapter.reload(items as ArrayList)
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
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerListView.adapter = listAdapter

        initSelectionTracker()

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            usersList = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            listAdapter.reload(usersList)
            applySearchFilter(binding.searchEditText.text.toString())
        } else {
            fetch({ items ->
                usersList =  items
                listAdapter.reload(usersList as ArrayList)
                applySearchFilter(binding.searchEditText.text.toString())
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        usersList?.let {
            outState.putParcelableArrayList(ARG_LIST, usersList as ArrayList<User>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSelectionTracker(){
        tracker = SelectionTracker.Builder(
            "favourite-selection-tracker",
            binding.recyclerListView,
            FavouritesListAdapter.KeyProvider(binding.recyclerListView),
            FavouritesListAdapter.DetailsLookup(binding.recyclerListView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        listAdapter.selectionTracker = tracker

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    if (tracker!!.hasSelection()) {
                        if (actionMode == null) {
                            actionMode = requireActivity().startActionMode(actModeCallback)
                        }
                        actionMode?.title = "${tracker?.selection?.size()} выбрано"
                    } else {
                        actionMode?.finish()
                    }
                }
            })
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

    private fun applySearchFilter(text : String?) {
        searchProvider.filter(usersList, text) { filtered->
            listAdapter.update(filtered)
        }
    }
}