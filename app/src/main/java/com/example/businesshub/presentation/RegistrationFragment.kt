package com.example.businesshub.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentLoginBinding
import com.example.businesshub.databinding.FragmentRegistrationBinding
import com.parse.ParseUser
import kotlinx.coroutines.launch


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        binding.signUp.setOnClickListener {

            val id = binding.id.text.toString()
            val password = binding.password.text.toString()
            val passwordConfirm = binding.passwordConfirm.text.toString()
            val email = binding.email.text.toString()

            if (!checkPassword(password, passwordConfirm)) {
                Toast.makeText(this.activity, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.signUp(id,password,email)

        }

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel.isSigned.collect{
                    if (it==true){
                        viewModel.userData.collect{ user->
                            val bundle = Bundle()
                            bundle.putParcelable("user",user)
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_registrationFragment_to_homeFragment, bundle)
                        }
                    }
                }

            }

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private fun checkPassword(password: String, passwordConfirm: String): Boolean {
        if (password != passwordConfirm) return false
        return true
    }

    private fun toHomeFragment() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_registrationFragment_to_homeFragment)
    }

    private fun creeateUser(name: String, password: String, email: String): ParseUser {
        val user = ParseUser()
        user.username = name
        user.setPassword(password)
        user.email = email
        return user
    }


}