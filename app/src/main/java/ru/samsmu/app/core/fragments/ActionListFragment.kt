/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 27 Jun 2025
 */

package ru.samsmu.app.core.fragments

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.View
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import ru.samsmu.app.core.adapters.ActionListAdapter

abstract class ActionListFragment<T : SelectableItem, A : ActionListAdapter<T>> : ListFragment<T, A>() {

    companion object {
        const val ARG_ITEM              = "action_item"
        const val ARG_ITEM_LIST_CHANGED = "action_list_changed"
    }

    protected var actionMode: ActionMode? = null

    protected var tracker: SelectionTracker<Long>? = null

    protected var actionMenu: Menu? = null

    protected lateinit var actionModeCallback : ActionMode.Callback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSelectionTracker()
    }

    private fun initSelectionTracker(){
        tracker = SelectionTracker.Builder(
            "favourite-selection-tracker",
            getRecyclerView(),
            ActionListAdapter.KeyProvider(getRecyclerView()),
            ActionListAdapter.DetailsLookup(getRecyclerView()),
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
                            actionMode = requireActivity().startActionMode(actionModeCallback)
                        }
                        actionMode?.title = "${tracker?.selection?.size()} выбрано"
                    } else {
                        actionMode?.finish()
                    }
                }
            })
    }
}