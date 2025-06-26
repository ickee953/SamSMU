/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 26 Jun 2025
 */

package ru.samsmu.app.core

import android.content.Context
import androidx.appcompat.app.AlertDialog
import ru.samsmu.app.R

fun showConfirmDialog(
    context: Context,
    titleResourceId : Int,
    messageResourceId : Int,
    confirmCallback: () -> Unit
){
    val builder = AlertDialog.Builder(context)

    builder.setTitle(titleResourceId)
    builder.setMessage(messageResourceId)

    builder.setPositiveButton(R.string.dialog_button_yes) { _, _ ->
        confirmCallback()
    }

    builder.setNegativeButton(R.string.dialog_button_no) { _, _ ->

    }

    builder.show()
}