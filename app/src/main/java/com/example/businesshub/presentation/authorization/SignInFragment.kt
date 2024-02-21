package com.example.businesshub.presentation.authorization

import android.os.Bundle
import android.util.Log
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
import com.example.businesshub.databinding.FragmentSignInBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph_xml)

    private val barcodeLauncher = registerForActivityResult(ScanContract())
    { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@SignInFragment.activity, "Cancelled", Toast.LENGTH_LONG).show()
            return@registerForActivityResult
        }
        val (email, password) = parseQr(result.contents)
        viewModel.signIn(email, password)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.signIn.setOnClickListener {
            setProgressBar(true)
            viewModel.signIn(
                binding.loginText.text.toString(),
                binding.passwordText.text.toString()
            )
        }

        binding.signInQr.setOnClickListener {
            barcodeLauncher.launch(createOptions())
        }

        binding.signUp.setOnClickListener {
            toSignUpFragment()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exceptionState
                    .collect {
                        Log.d("signIn","collected $it")
                        Toast.makeText(this@SignInFragment.context, it, Toast.LENGTH_SHORT).show()
                        setProgressBar(false)
                    }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authSharedState
                    .filterNotNull()
                    .collect {
                        when (it) {
                            AuthState.SUCCESS -> navigateToHome()
                            AuthState.MINIMAL_SUCCESS -> navigateToPerson()
                            AuthState.FAILURE -> {
                                Toast.makeText(
                                    this@SignInFragment.context,
                                    resources.getText(R.string.login_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                setProgressBar(false)
                            }
                        }
                    }
            }
        }

        return binding.root
    }

    private fun setProgressBar(b: Boolean) {
        binding.layout.visibility = if (b) View.GONE else View.VISIBLE
        binding.progressCircular.visibility = if (b) View.VISIBLE else View.GONE
    }


    private fun parseQr(contents: String): Pair<String, String> {
        val (login, password) = contents.split(" ")
        return Pair(login.split(':')[1], password.split(':')[1])
    }


    private fun toSignUpFragment() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    private fun navigateToPerson() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_signInFragment_to_personInfoFragment)
    }

    private fun navigateToHome() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_signInFragment_to_homeFragment)
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