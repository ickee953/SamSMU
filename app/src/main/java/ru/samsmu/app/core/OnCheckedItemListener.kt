package ru.samsmu.app.core

import android.view.View

interface OnCheckedItemListener<T> {

    /**
     * Called when the checked state of item has changed.
     *
     * @param itemObject The item object whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    fun onCheckedChanged(itemObject: T?, view: View, isChecked: Boolean)

}