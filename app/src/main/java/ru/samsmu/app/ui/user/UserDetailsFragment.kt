package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUserDetailsBinding
import ru.samsmu.app.ui.favorite.UserFavouriteCheckedProvider

class UserDetailsFragment : Fragment() {

    companion object {
        const val ARG_USER = "user"
    }

    private lateinit var userViewModel : UserViewModel

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

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

        user?.let {

            val userFavouriteCheckedProvider = UserFavouriteCheckedProvider(this, userViewModel)

            binding.favouriteBtn.setOnClickListener { view ->

                if(it.isFavourite == 1){
                    userFavouriteCheckedProvider.onCheckedChanged(user, view, false)
                } else {
                    userFavouriteCheckedProvider.onCheckedChanged(user, view,true)
                }

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user?.let { updateUI(it) }
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
        val title = "${user.firstName} ${user.lastName} ${user.maidenName}"
        (activity as AppCompatActivity).supportActionBar?.title = title
        binding.email.text   = "${user.email}"
        binding.age.text     = "${resources.getString(R.string.age)}: ${user.age}"
        binding.phone.text   = "${user.phone}"
        binding.address.text = "${user.address}"

        binding.favouriteBtn.isChecked = user.isFavourite == 1

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
    }

}