package com.example.businesshub.presentation.create_company

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentFinishCompanyCreatingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompanyPictureFragment : Fragment() {

    private var _binding: FragmentFinishCompanyCreatingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompanyCreationViewModel by  navGraphViewModels(R.id.auth_nav_graph_xml)
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.setPictureUri(uri)
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishCompanyCreatingBinding.inflate(inflater, container, false)

        binding.next.setOnClickListener {
            setProgressBar(true)
            viewModel.createCompany()
        }

        binding.back.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureUri
                    .filterNotNull()
                    .collect {
                        Glide.with(binding.image).load(it).into(binding.image)
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result
                    .collect {
                        if (it) {
                            navigateToHome()
                        } else {
                            Toast.makeText(
                                this@CompanyPictureFragment.context,
                                "Ошибка загрузки, попробуйте еще раз",
                                Toast.LENGTH_SHORT
                            ).show()
                            setProgressBar(false)
                        }
                    }
            }
        }

        binding.pickImage.setOnClickListener {
            getContent.launch("image/*")
        }

        return binding.root
    }

    private fun navigateToHome() {
        Navigation.findNavController(binding.root).navigate(R.id.action_finishCompanyCreatingFragment_to_homeFragment)
    }


    private fun setProgressBar(b: Boolean) {
        binding.layout.visibility = if (b) View.GONE else View.VISIBLE
        binding.progressCircular.visibility = if (b) View.VISIBLE else View.GONE
    }
}