/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 19 Jun 2025
 */

package ru.samsmu.app.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem import android.view.View
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
import ru.samsmu.app.databinding.FragmentFavoriteBinding
import ru.samsmu.app.R
import ru.samsmu.app.ui.Fetchable

class FavoriteFragment : Fragment(), Fetchable {

    companion object {
        const val ARG_LIST                      = "favourites_list"
        const val ARG_USER                      = "favourites_item"
        const val ARG_FAVOURITE_LIST_CHANGED    = "favourite_list_changed"
    }

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userViewModel : UserViewModel

    private lateinit var listAdapter : FavoritesListAdapter

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

        listAdapter = FavoritesListAdapter(R.layout.user_list_item) { itemView ->
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
            listAdapter.setDataset(savedInstanceState.getParcelableArrayList(ARG_LIST)!!)
        }

        setFragmentResultListener(ARG_FAVOURITE_LIST_CHANGED) { _, _->
            fetch({ items ->
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
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.recyclerListView.adapter = listAdapter

        initSelectionTracker()

        return root
    }

    private fun initSelectionTracker(){
        tracker = SelectionTracker.Builder(
            "favourite-selection-tracker",
            binding.recyclerListView,
            FavoritesListAdapter.KeyProvider(binding.recyclerListView),
            FavoritesListAdapter.DetailsLookup(binding.recyclerListView),
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
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            listAdapter.reload(savedInstanceState.getParcelableArrayList(ARG_LIST)!!)
        } else {
            fetch({ items ->
                listAdapter.reload(items as ArrayList)
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ARG_LIST, listAdapter.getDataset() as ArrayList<User>)
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