/**
 * © Panov Vitaly 2025 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Panov Vitaly 27 Jun 2025
 */

package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import ru.samsmu.app.R
import ru.samsmu.app.core.providers.FavourableCallbackProvider
import ru.samsmu.app.core.providers.FavourableCallbackProviderImpl
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUserDetailsBinding
import ru.samsmu.app.ui.menu.DetailsMenuProvider
import ru.samsmu.app.ui.menu.MenuProvided

class UserDetailsFragment : Fragment() {

    companion object {
        const val ARG_USER = "user"
    }

    private lateinit var userViewModel : UserViewModel

    private lateinit var favouriteCallbackProvider: FavourableCallbackProvider<User>

    private var user: User? = null

    private val detailsMenuProvider = object : DetailsMenuProvider(R.menu.details_menu){
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            super.onCreateMenu(menu, menuInflater)
            user?.let {
                val menuItem = menu.findItem(R.id.action_favourite)

                if(user!!.isFavourite == 1){
                    menuItem.icon = resources.getDrawable(R.drawable.baseline_star_24, null)
                } else {
                    menuItem.icon = resources.getDrawable(R.drawable.baseline_star_outline_24, null)
                }
            }

        }

        override fun actionMenuFavourite(){
            user?.let { u->
                if(u.isFavourite == 1){
                    favouriteCallbackProvider.removeFromFavourites(u) {
                        u.isFavourite = 0
                        requireActivity().invalidateMenu()
                        Toast.makeText(requireContext(), R.string.removed_from_favourites, Toast.LENGTH_LONG).show()
                    }
                } else {
                    favouriteCallbackProvider.addToFavourites(u) {
                        u.isFavourite = 1
                        requireActivity().invalidateMenu()
                        Toast.makeText(requireContext(), R.string.added_to_favourites, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        favouriteCallbackProvider = FavourableCallbackProviderImpl(this, userViewModel)

        arguments?.let {
            if(it.containsKey(ARG_USER)){
                user = it.getParcelable(ARG_USER)
            }
        }
    }

    private var _binding: FragmentUserDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setupActionBar(){
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val navHostFragment =
            (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_users, R.id.navigation_favorite
            )
        )

        (activity as AppCompatActivity).setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionBar()

        user?.let { updateUI(it) }
    }

    override fun onResume(){
        super.onResume()
        requireActivity().addMenuProvider(detailsMenuProvider)
    }

    override fun onPause(){
        requireActivity().removeMenuProvider(detailsMenuProvider)
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if( savedInstanceState != null && savedInstanceState.containsKey(ARG_USER) ){
            val user: User? = savedInstanceState.getParcelable(ARG_USER)
            user?.let { updateUI(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(ARG_USER, user)
        super.onSaveInstanceState(outState)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: User){
        val title   = "${user.firstName} ${user.lastName} ${user.maidenName}"
        val age     = "${resources.getString(R.string.age)}: ${user.age}"

        binding.collapsingToolbar.title = title
        binding.collapsingToolbar.subtitle = age

        binding.email.text   = "${user.email}"
        binding.phone.text   = "${user.phone}"
        binding.address.text = "${user.address}"

        val imageLoader = ImageLoader.Builder(requireContext())
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()

        if( user.image != null && user.image != ""){
            binding.imageView.load( user.image, imageLoader )
        } else {
            binding.imageView.visibility = View.GONE
        }

        requireActivity().invalidateMenu()
    }

}