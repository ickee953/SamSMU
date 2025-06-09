package ru.samsmu.app.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.launch
import ru.samsmu.app.R
import ru.samsmu.app.data.model.User
import ru.samsmu.app.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : Fragment() {

    companion object {
        const val ARG_USER = "user"
    }

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

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

        binding.favoriteBtn.setOnClickListener {

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

    private fun loadUser(){

    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(user: User){
        binding.name.text    = "${user.firstName} ${user.lastName} ${user.maidenName}"
        binding.email.text   = user.email

        val imageLoader = ImageLoader.Builder(requireContext())
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .build()

        if( user.image != null && user.image != ""){
            binding.imageView.load( user.image, imageLoader ){
                transformations(RoundedCornersTransformation(25F))
            }
        } else {
            binding.imageView.load(R.drawable.baseline_person_24){
                transformations(RoundedCornersTransformation(25F))
            }
        }
    }

}