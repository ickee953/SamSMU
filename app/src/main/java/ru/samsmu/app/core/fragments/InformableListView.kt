/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 10 Jul 2025
 */

package ru.samsmu.app.core.fragments

interface InformableListView {

    fun showEmptyListInfoView()

    fun showErrorInfoView(message : String)

    fun showListView()

}