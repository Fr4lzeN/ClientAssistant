package com.example.businesshub.presentation.create_person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentPersonInfoBinding


class PersonInfoFragment : Fragment() {

    var _binding: FragmentPersonInfoBinding? = null
    val binding get() = _binding!!
    val viewModel: PersonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonInfoBinding.inflate(inflater, container, false)

        viewModel.token = arguments?.getString("token")
        viewModel.user = arguments?.getParcelable("user")!!


        binding.name.doAfterTextChanged {
            viewModel.setName(it.toString())
            checkFields()
        }
        binding.surname.doAfterTextChanged {
            viewModel.setSurname(it.toString())
            checkFields()
        }
        binding.email.doAfterTextChanged {
            viewModel.setEmail(it.toString())
            checkFields()
        }
        binding.phone.doAfterTextChanged {
            viewModel.setPhone(it.toString())
            checkFields()
        }

        binding.next.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_personInfoFragment_to_personPictureFragment)
        }

        return binding.root
    }

    private fun checkFields() {
        val name = checkName()
        val surname = checkSurname()
        val email = checkEmail()
        val phone = checkPhone()
        binding.next.isEnabled = name && surname && email && phone
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
        binding.name.error = "Имя должно состоять минимум из 2 символов"
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
        binding.surname.error = "Фамилия должно состоять минимум из 2 символов"
        return false
    }

    private fun checkEmail(): Boolean {
        val email = binding.email.text.toString()
        if (email.isEmpty()) {
            return false
        }
        if (Regex("^.+@[a-zA-Z\\d]+.[a-zA-Z]{2,}$").matches(email)) {
            binding.email.error = null
            return true
        }
        binding.email.error = "Неправильный формат почты"
        return false
    }

    private fun checkPhone(): Boolean {
        val phone = binding.phone.text.toString()
        if (phone.isEmpty()) {
            return false
        }
        if (Regex("^(\\+7|8)\\d{10}\$\n").matches(phone)) {
            binding.phone.error = null
            return true
        }
        binding.phone.error = "Неправильный формат номера телефона"
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}