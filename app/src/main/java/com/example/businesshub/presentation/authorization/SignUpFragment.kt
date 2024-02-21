package com.example.businesshub.presentation.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.businesshub.R
import com.example.businesshub.core.AuthState
import com.example.businesshub.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph_xml)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.signUp.setOnClickListener {
            val (email, password, passwordConfirm) = getData()
            if (!checkEmail(email) || !checkPassword(password, passwordConfirm)) {
                return@setOnClickListener
            }
            setProgressBar(true)
            viewModel.signUp(email, password)
        }

        binding.back.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exceptionState
                    .collect {
                        Toast.makeText(this@SignUpFragment.context, it, Toast.LENGTH_SHORT).show()
                        setProgressBar(false)
                    }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authSharedState
                    .filterNotNull()
                    .collect {
                        if (it == AuthState.MINIMAL_SUCCESS) {
                            navigateToPerson()
                        } else {
                            Toast.makeText(
                                this@SignUpFragment.context,
                                resources.getResourceName(R.string.registration_error),
                                Toast.LENGTH_SHORT
                            ).show()
                            setProgressBar(false)
                        }
                    }
            }
        }

        return binding.root
    }

    private fun checkEmail(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }
        if (Regex("^.+@[a-zA-Z\\d]+\\.[a-zA-Z]{2,}$").matches(email)) {
            binding.email.error = null
            return true
        }
        binding.email.error = resources.getResourceName(R.string.email_format_error)
        return false
    }


    private fun setProgressBar(b: Boolean) {
        binding.layout.visibility = if (b) View.GONE else View.VISIBLE
        binding.progressCircular.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun navigateToPerson() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_signUpFragment_to_personInfoFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPassword(password: String, passwordConfirm: String): Boolean {
        if (password != passwordConfirm) {
            Toast.makeText(
                this.activity,
                resources.getResourceName(R.string.password_error),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun getData(): Array<String> {
        return arrayOf(
            binding.email.text.toString(),
            binding.password.text.toString(),
            binding.passwordConfirm.text.toString(),
        )
    }


}