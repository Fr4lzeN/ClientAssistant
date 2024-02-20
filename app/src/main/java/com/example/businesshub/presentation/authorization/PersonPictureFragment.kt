package com.example.businesshub.presentation.authorization

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.example.businesshub.R
import com.example.businesshub.core.AuthState
import com.example.businesshub.databinding.FragmentPersonPictureBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PersonPictureFragment : Fragment() {

    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph_xml)

    private var _binding: FragmentPersonPictureBinding? = null
    val binding get() = _binding!!

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.setPictureUri(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonPictureBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            navigateBack()
        }

        binding.finish.setOnClickListener {
            setProgressBar(true)
            viewModel.uploadPersonInfo()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureUri
                    .filterNotNull()
                    .collect {
                        Glide.with(this@PersonPictureFragment).load(it).into(binding.image)
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exceptionState
                    .collect {
                        Toast.makeText(this@PersonPictureFragment.context, it, Toast.LENGTH_SHORT)
                            .show()
                        setProgressBar(false)
                    }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState
                    .collect {
                        if (it == AuthState.SUCCESS) {
                            navigateToHome()
                        } else {
                            Toast.makeText(
                                this@PersonPictureFragment.context,
                                "Ошибка, попробуйте еще раз",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }

        }
        binding.pickPicture.setOnClickListener {
            getContent.launch("image/*")
        }
        return binding.root
    }

    private fun setProgressBar(b: Boolean) {
        binding.layout.visibility = if (b) View.GONE else View.VISIBLE
        binding.progressCircular.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun navigateToHome() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_personPictureFragment_to_homeFragment)
    }

    private fun navigateBack() {
        Navigation.findNavController(binding.root).popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}