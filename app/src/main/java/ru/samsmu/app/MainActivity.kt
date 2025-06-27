/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 7 Jun 2025
 */

package ru.samsmu.app

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ru.samsmu.app.databinding.ActivityMainBinding
import androidx.navigation.ui.navigateUp
import ru.samsmu.app.ui.menu.MainMenuProvider
import ru.samsmu.app.ui.menu.MenuProvided

class MainActivity : AppCompatActivity(), MenuProvided {

    private lateinit var binding: ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navController: NavController

    private lateinit var navHostFragment: NavHostFragment

    private val mainMenuProvider = object : MainMenuProvider(){
        override fun actionMenuSettings() {
            //start settings activity
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)

            startActivity(intent)
        }
    }

    override fun getMenuProvider(): MenuProvider {
        return mainMenuProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_users, R.id.navigation_favorite
            )
        )

        binding.navView.setupWithNavController(navController)

        addMenuProvider(mainMenuProvider)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val fragment = navHostFragment.childFragmentManager.fragments[0]
        if(fragment != null && navController.currentDestination?.id == R.id.user_details_fragment){
            super.onBackPressed()
        } else {
            finish()
        }
    }
}