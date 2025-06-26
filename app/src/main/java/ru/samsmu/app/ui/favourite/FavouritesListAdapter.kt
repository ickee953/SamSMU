/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 9 Jun 2025
 */

package ru.samsmu.app.ui.favourite

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.CheckBox
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import ru.samsmu.app.R
import ru.samsmu.app.data.model.User
import ru.samsmu.app.core.ReloadableAdapter

class FavouritesListAdapter(
    resId : Int,
    private val onClickListener: View.OnClickListener
): ReloadableAdapter<User>(resId) {

    class DetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>(){

        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null

            val holder = recyclerView.getChildViewHolder(view)
            return if (holder is ItemViewHolder){
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
            return if (holder is ItemViewHolder) {
                holder.bindingAdapterPosition
            } else {
                RecyclerView.NO_POSITION
            }
        }
    }

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var nameTextView: TextView  = view.findViewById(R.id.name)
        var emailTextView: TextView = view.findViewById(R.id.email)
        var imageView: ImageView    = view.findViewById(R.id.image_view)
        var favoriteBtn: CheckBox   = view.findViewById(R.id.favourite_btn)
        var selection: View         = view.findViewById(R.id.selection_view)

        val imageLoader = ImageLoader.Builder(view.context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()
    }

    init {
        setHasStableIds(true)
    }

    var selectionTracker: SelectionTracker<Long>? = null

    override fun getItemId(position: Int): Long {
        if(position < 0 || position >= items.size) throw IndexOutOfBoundsException()

        return items[position].id
    }

    override fun createViewHolder(view: View) = ItemViewHolder(view)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = items[position]

        holder as ItemViewHolder

        selectionTracker?.let {
            if (it.isSelected(user.id)) {
                holder.selection.visibility = View.VISIBLE
            } else {
                holder.selection.visibility = View.INVISIBLE
            }
        }

        holder.nameTextView.text = "${user.firstName} ${user.lastName} ${user.maidenName}"

        holder.emailTextView.text   = user.email

        if( user.image != null && user.image != ""){
            holder.imageView.load( user.image, holder.imageLoader ){
                transformations(RoundedCornersTransformation(25F))
            }
        } else {
            holder.imageView.load(R.drawable.baseline_person_24){
                transformations(RoundedCornersTransformation(25F))
            }
        }

        holder.favoriteBtn.visibility = View.GONE

        holder.itemView.tag = user

        holder.itemView.setOnClickListener(onClickListener)
    }
}