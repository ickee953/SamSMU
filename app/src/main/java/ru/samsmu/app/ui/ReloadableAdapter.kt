/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 9 Jun 2025
 */

package ru.samsmu.app.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ReloadableAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    protected var items: MutableList<T> = ArrayList()

    abstract fun getResourceLayoutId() : Int

    abstract fun createViewHolder( view: View) : RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getResourceLayoutId(), parent, false)

        return createViewHolder( view )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reload(dataset: Collection<T>?){
        if(setDataset(dataset)) notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(item : T) : Boolean{

        //get index of element
        val index = items.indexOfFirst { it == item }

        if(index < 0 || index >= items.size) throw IndexOutOfBoundsException()

        if(items.remove(item)) {

            notifyItemRemoved(index)

            return true
        }
        return false
    }

    fun getDataset() = items

    fun setDataset(dataset: Collection<T>?) : Boolean{
        if(dataset != null) {
            items.clear()
            items.addAll(dataset)

            return true
        }
        return false
    }
}