/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 27 Jun 2025
 */

package ru.samsmu.app.core.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import ru.samsmu.app.core.adapters.ReloadableAdapter
import ru.samsmu.app.core.adapters.ListAdaptable
import ru.samsmu.app.core.providers.SearchableFilterProvider
import ru.samsmu.app.core.providers.SearchableFilterProviderImp
import ru.samsmu.app.data.model.User

abstract class ListFragment<T, A : ReloadableAdapter<T>> : Fragment(), ListAdaptable<T>, Fetchable<T>, Searchable {

    abstract fun applySearchFilter()

    abstract fun getRecyclerView() : RecyclerView

    abstract fun createListAdapter() : A

    abstract fun createViewModel() : AndroidViewModel

    protected lateinit var viewModel : AndroidViewModel

    protected var list : Collection<T>? = null

    protected lateinit var listAdapter : A

    private lateinit var searchFilterProvider : SearchableFilterProvider<T>

    companion object {
        const val ARG_LIST = "users_list"
    }

    protected fun removeListItem( item : T ) {
        listAdapter.remove(item)
        list = listAdapter.getDataset()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = createViewModel()

        listAdapter = createListAdapter()

        searchFilterProvider = SearchableFilterProviderImp()

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            list = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            list?.let {
                setDataset(list!!)
            }
        }

	    setFragmentResultListener(ActionListFragment.ARG_ITEM_LIST_CHANGED) { _, _->
            fetch({ items ->
                list = items
                listAdapter.reload(items as ArrayList)
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            })
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_LIST) ){
            list = savedInstanceState.getParcelableArrayList(ARG_LIST)!!
            list?.let {
                reloadDataset(list!!)
                applySearchFilter()
            }
        } else {
            fetch({ items ->
                list =  items
                reloadDataset(list!!)
                applySearchFilter()
            }, { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        list?.let {
            outState.putParcelableArrayList(ARG_LIST, it as ArrayList<User>)
        }
    }

    override fun filter(text: String?) {
        searchFilterProvider.filter(list, text) { filtered->
            updateDataset(filtered)
        }
    }

    override fun updateDataset(dataset: Collection<T>) {
        listAdapter.update(dataset)
    }

    override fun reloadDataset(dataset: Collection<T>) {
        listAdapter.reload(dataset)
    }

    override fun setDataset(dataset: Collection<T>) {
        listAdapter.setDataset(dataset)
    }
}
