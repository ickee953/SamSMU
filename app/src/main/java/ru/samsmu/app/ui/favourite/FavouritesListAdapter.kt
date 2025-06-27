/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 9 Jun 2025
 */

package ru.samsmu.app.ui.favourite

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import ru.samsmu.app.R
import ru.samsmu.app.data.model.User
import ru.samsmu.app.core.fragments.ActionListAdapter

class FavouritesListAdapter(
    resId : Int,
    private val onClickListener: View.OnClickListener
): ActionListAdapter<User>(resId) {

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