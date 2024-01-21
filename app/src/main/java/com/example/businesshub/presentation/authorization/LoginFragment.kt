package com.example.businesshub.presentation.authorization

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentLoginBinding
import com.example.businesshub.domain.model.User
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by activityViewModels()

    private val barcodeLauncher = registerForActivityResult(ScanContract())
    { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@LoginFragment.activity, "Cancelled", Toast.LENGTH_LONG).show()
            return@registerForActivityResult
        }
        val (username, password) = parseQr(result.contents)
        viewModel.signIn(username, password)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.signIn.setOnClickListener {
            viewModel.signIn(
                binding.loginText.text.toString(),
                binding.passwordText.text.toString()
            )
        }


        binding.signInQr.setOnClickListener {
            barcodeLauncher.launch(createOptions())
        }

        binding.signUp.setOnClickListener {
            toRegistrationFragment()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSigned.collect { isSigned ->
                    if (isSigned == true) {
                        viewModel.userData.collect {
                            if (it?.personId == null) {
                                navigateToPerson(it)
                                return@collect
                            }
                            if (it.companyId == null) {
                                navigateToCompany(it)
                                return@collect
                            }
                            navigateToHome(it)
                        }
                    }
                    if (isSigned == false) {
                        Toast.makeText(
                            this@LoginFragment.context,
                            "Ошибка входа",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        return binding.root
    }

    private fun navigateToCompany(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        Navigation
            .findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_companyNameFragment, bundle)
    }

    private fun navigateToPerson(user: User?) {
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        Navigation
            .findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_personInfoFragment, bundle)
    }

    private fun navigateToHome(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        Navigation
            .findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_homeFragment, bundle)
    }

    private fun parseQr(contents: String): Pair<String, String> {
        val (login, password) = contents.split(" ")
        return Pair(login.split(':')[1], password.split(':')[1])
    }


    private fun toRegistrationFragment() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_registrationFragment)
    }


    private fun createOptions(): ScanOptions {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Отсканируйте QR код")
        options.setCameraId(0)
        options.setOrientationLocked(true)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        return options
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}