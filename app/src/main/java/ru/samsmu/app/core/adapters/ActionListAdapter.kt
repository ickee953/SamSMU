/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 27 Jun 2025
 */

package ru.samsmu.app.core.adapters

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import ru.samsmu.app.core.fragments.SelectableItem

abstract class ActionListAdapter<T: SelectableItem>(
    resourceId : Int
) : ReloadableAdapter<T>(resourceId) {

    class DetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>(){

        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null

            val holder = recyclerView.getChildViewHolder(view)
            return if (holder is RecyclerView.ViewHolder){
                object : ItemDetails<Long>() {
                    override fun getPosition(): Int {
                        return holder.bindingAdapterPosition
                    }

                    override fun getSelectionKey(): Long {
                        return holder.itemId
                    }
                }
            } else {
                null
            }
        }
    }

    class KeyProvider(private val recyclerView: RecyclerView) : ItemKeyProvider<Long>(SCOPE_MAPPED){
        override fun getKey(position: Int): Long {
            val holder = recyclerView.findViewHolderForAdapterPosition(position)
            return holder?.itemId ?: throw IllegalStateException("No Holder")
        }

        override fun getPosition(key: Long): Int {
            val holder = recyclerView.findViewHolderForItemId(key)
            return if (holder is RecyclerView.ViewHolder) {
                holder.bindingAdapterPosition
            } else {
                RecyclerView.NO_POSITION
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    var selectionTracker: SelectionTracker<Long>? = null

    override fun getItemId(position: Int): Long {
        if(position < 0 || position >= items.size) throw IndexOutOfBoundsException()

        return items[position].getSelectionId()
    }

}