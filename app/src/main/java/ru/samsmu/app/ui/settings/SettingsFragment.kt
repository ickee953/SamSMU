/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 8 Jun 2025
 */

package ru.samsmu.app.ui.settings

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import ru.samsmu.app.databinding.FragmentSettingsBinding
import ru.samsmu.app.R

class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = resources.getStringArray(R.array.muscles)

        val adapter = ArrayAdapter(requireContext(), R.layout.select_list_item, items)

        binding.autoCompleteSensor1.setAdapter(adapter)
        binding.autoCompleteSensor2.setAdapter(adapter)
        binding.autoCompleteSensor3.setAdapter(adapter)
        binding.autoCompleteSensor4.setAdapter(adapter)
        binding.autoCompleteSensor5.setAdapter(adapter)
        binding.autoCompleteSensor6.setAdapter(adapter)
        binding.autoCompleteSensor7.setAdapter(adapter)
        binding.autoCompleteSensor8.setAdapter(adapter)

        binding.saveBtn.setOnClickListener {
            val text =  "Sensor 1: ${binding.autoCompleteSensor1.text}\n" +
                        "Sensor 2: ${binding.autoCompleteSensor2.text}\n" +
                        "Sensor 3: ${binding.autoCompleteSensor3.text}\n" +
                        "Sensor 4: ${binding.autoCompleteSensor4.text}\n" +
                        "Sensor 5: ${binding.autoCompleteSensor5.text}\n" +
                        "Sensor 6: ${binding.autoCompleteSensor6.text}\n" +
                        "Sensor 7: ${binding.autoCompleteSensor7.text}\n" +
                        "Sensor 8: ${binding.autoCompleteSensor8.text}\n"

            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }
    }

}