/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 9 Jun 2025
 */

package ru.samsmu.app.ui

import androidx.recyclerview.widget.RecyclerView

abstract class ReloadableAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    abstract fun getDataset(): List<T>?

    abstract fun setDataset(dataset: List<T>?)

    abstract fun reload(dataset: List<T>?)

}