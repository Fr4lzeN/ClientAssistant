package com.example.businesshub.presentation.create_company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentCompanyNameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanyNameFragment : Fragment() {

    private var _binding: FragmentCompanyNameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateCompanyViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyNameBinding.inflate(inflater, container, false)

        viewModel.setToken(arguments?.getString("token"))
        viewModel.setUser(arguments?.getParcelable("user")!!)

        binding.name.setText(viewModel.name.value ?: "")
        binding.desc.setText(viewModel.desc.value ?: "")
        binding.adress.setText(viewModel.addr.value ?: "")

        binding.name.doAfterTextChanged {
            viewModel.setName(it.toString())
            checkFields()
        }

        binding.desc.doAfterTextChanged {
            viewModel.setDesc(it.toString())
            checkFields()
        }

        binding.adress.doAfterTextChanged {
            viewModel.setAddr(it.toString())
            checkFields()
        }

        binding.next.setOnClickListener {

        }

        binding.back.setOnClickListener {
            navigateBack()
        }

        return binding.root
    }

    private fun navigateBack() {
        Navigation.findNavController(binding.root).popBackStack()
    }

    private fun checkFields() {
        binding.next.isEnabled = checkName() && checkDesc() && checkAddr()
    }

    private fun checkAddr(): Boolean {
        val addr = binding.adress.text.toString()
        if (addr.length <= 5) {
            if (addr.isNotEmpty()) {
                binding.adress.error =  resources.getResourceName(R.string.addres_error)
            }
            return false
        }
        binding.adress.error = null
        return true
    }

    private fun checkDesc(): Boolean {
        val desc = binding.desc.text.toString()
        if (desc.length < 10) {
            if (desc.isNotEmpty()) {
                binding.desc.error =  resources.getResourceName(R.string.description_error)
            }
            return false
        }
        binding.desc.error = null
        return true
    }

    private fun checkName(): Boolean {
        val name = binding.name.text.toString()
        if (name.length < 3) {
            if (name.isNotEmpty()) {
                binding.name.error =  resources.getResourceName(R.string.company_name_error)
            }
            return false
        }
        binding.name.error = null
        return true
    }

}