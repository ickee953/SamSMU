/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import ru.samsmu.app.data.model.User
import androidx.recyclerview.widget.RecyclerView
import ru.samsmu.app.R
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import android.widget.CheckBox
import ru.samsmu.app.core.OnCheckedItemListener
import ru.samsmu.app.core.adapters.ReloadableAdapter

class UsersListAdapter(
    resId : Int,
    private val onClickListener: View.OnClickListener,
    private val onCheckedItemListener: OnCheckedItemListener<User>
): ReloadableAdapter<User>(resId) {

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var nameTextView: TextView      = view.findViewById(R.id.name)
        var emailTextView: TextView     = view.findViewById(R.id.email)
        var imageView: ImageView        = view.findViewById(R.id.image_view)
        var favouriteBtn: CheckBox       = view.findViewById(R.id.favourite_btn)

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

        holder.nameTextView.text    = "${user.firstName} ${user.lastName} ${user.maidenName}"
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

        holder.favouriteBtn.isChecked = user.isFavourite == 1

        holder.itemView.tag = user

        holder.itemView.setOnClickListener(onClickListener)
        holder.favouriteBtn.setOnClickListener{ view ->
            if(user.isFavourite == 1){
                onCheckedItemListener.onCheckedChanged(user, view, false)
            } else {
                onCheckedItemListener.onCheckedChanged(user, view,true)
            }
        }
    }
}