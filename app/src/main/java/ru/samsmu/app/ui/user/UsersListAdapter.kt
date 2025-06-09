/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import ru.samsmu.app.data.model.User
import androidx.recyclerview.widget.RecyclerView
import ru.samsmu.app.R
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import ru.samsmu.app.ui.ReloadableList

class UsersListAdapter(
    private val users: MutableList<User>,
    private val onClickListener: View.OnClickListener,
    private val favoriteClickListener: View.OnClickListener
): RecyclerView.Adapter<UsersListAdapter.ItemViewHolder>(), ReloadableList<User> {

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var nameTextView: TextView      = view.findViewById(R.id.name)
        var emailTextView: TextView     = view.findViewById(R.id.email)
        var imageView: ImageView        = view.findViewById(R.id.image_view)
        var favoriteBtn: ToggleButton   = view.findViewById(R.id.favorite_btn)

        val imageLoader = ImageLoader.Builder(view.context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val userView = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)

        return ItemViewHolder(userView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = users[position]

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

        holder.itemView.tag = user

        holder.itemView.setOnClickListener(onClickListener)
        holder.favoriteBtn.setOnClickListener(favoriteClickListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun reloadUsers(dataset: List<User>?) {
        if(dataset != null){
            users.clear()
            users.addAll(dataset)
            notifyDataSetChanged()
        }
    }

}