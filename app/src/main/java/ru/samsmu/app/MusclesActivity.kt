/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 8 Jun 2025
 */

package ru.samsmu.app

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.samsmu.app.databinding.ActivityMusclesBinding

class MusclesActivity: AppCompatActivity() {

    private var _binding: ActivityMusclesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMusclesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

}