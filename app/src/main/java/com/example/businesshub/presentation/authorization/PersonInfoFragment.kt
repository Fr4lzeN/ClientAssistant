package com.example.businesshub.presentation.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentPersonInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonInfoFragment : Fragment() {

    private var _binding: FragmentPersonInfoBinding? = null
    val binding get() = _binding!!
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph_xml)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonInfoBinding.inflate(inflater, container, false)

        fillFields()


        binding.name.doAfterTextChanged {
            viewModel.setName(it.toString())
            checkFields()
        }
        binding.surname.doAfterTextChanged {
            viewModel.setSurname(it.toString())
            checkFields()
        }
        binding.phone.doAfterTextChanged {
            viewModel.setPhone(it.toString())
            checkFields()
        }
        binding.next.setOnClickListener {
            navigateToPersonPicture()

        }
        binding.back.setOnClickListener {
            navigateBack()
        }
        return binding.root
    }

    private fun fillFields() {
        binding.name.setText(viewModel.name.value?:"")
        binding.surname.setText(viewModel.surname.value?:"")
        binding.phone.setText(viewModel.phone.value?:"")
    }

    private fun navigateToPersonPicture() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_personInfoFragment_to_personPictureFragment)
    }

    private fun navigateBack() {
        Navigation.findNavController(binding.root).navigate(R.id.action_personInfoFragment_to_signInFragment)
    }

    private fun checkFields() {
        val name = checkName()
        val surname = checkSurname()
        val phone = checkPhone()
        binding.next.isEnabled = name && surname && phone
    }

    private fun checkName(): Boolean {
        val name = binding.name.text.toString()
        if (name.isEmpty()) {
            return false
        }
        if (binding.name.text.toString().length > 1) {
            binding.name.error = null
            return true
        }
        binding.name.error = resources.getResourceName(R.string.name_length_error)
        return false
    }

    private fun checkSurname(): Boolean {
        val surname = binding.surname.text.toString()
        if (surname.isEmpty()) {
            return false
        }
        if (binding.surname.text.toString().length > 1) {
            binding.surname.error = null
            return true
        }
        binding.surname.error = resources.getResourceName(R.string.surname_length_error)
        return false
    }

    private fun checkPhone(): Boolean {
        val phone = binding.phone.text.toString()
        if (phone.isEmpty()) {
            return false
        }
        if (Regex("^(\\+7|8)\\d{10}$").matches(phone)) {
            binding.phone.error = null
            return true
        }
        binding.phone.error = resources.getResourceName(R.string.phone_format_error)
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}