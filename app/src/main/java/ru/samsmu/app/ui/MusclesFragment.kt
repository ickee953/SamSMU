/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 8 Jun 2025
 */

package ru.samsmu.app.ui

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import ru.samsmu.app.databinding.FragmentMusclesBinding
import ru.samsmu.app.R

class MusclesFragment : Fragment() {

    private var _binding : FragmentMusclesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMusclesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = resources.getString(R.string.action_muscules)
    }

}