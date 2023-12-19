package com.example.businesshub.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.businesshub.R
import com.example.businesshub.data.data_source.DTO.GetUserDTO
import com.example.businesshub.data.data_source.DTO.UserDTO
import com.example.businesshub.databinding.FragmentLoginBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.math.log


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val viewModel: SignInViewModel by activityViewModels()

    private val barcodeLauncher = registerForActivityResult(ScanContract())
    { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@LoginFragment.activity, "Cancelled", Toast.LENGTH_LONG).show()
            return@registerForActivityResult
        }
        signIn(parseQr(result.contents))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.signIn.setOnClickListener {
            login(binding.loginText.text.toString(), binding.passwordText.text.toString())
        }

        binding.signInQr.setOnClickListener {
            barcodeLauncher.launch(createOptions())
        }

        binding.signUp.setOnClickListener {
            toRegistrationFragment()
        }

       GlobalScope.launch {

           val response = viewModel.userApiRepository.signUp(UserDTO("testApi","testApi","testApi@mail.ru"))
           if(response.isSuccessful){
               Log.d("test", "sign up ${response.body()?.createdAt}")
               val signIn = viewModel.userApiRepository.signIn("testApi","testApi")
               if (signIn.isSuccessful){
                   Log.d("test","sign in ${signIn.body()?.username}")
                   signIn.body()?.let {
                       Log.d("test","objetc id ${it.objectId}")
                       val getUser=viewModel.userApiRepository.getUser(it.objectId)
                       if (getUser.isSuccessful){
                            Log.d("test", "get user ${getUser.body()}")
                            viewModel.userApiRepository.logOut(it.sessionToken)
                       }else{
                           Log.d("test", "get user error")
                       }
                   }


               }else{
                   Log.d("test","sign in error")
               }
           }else{
               Log.d("test", "error signUp")
           }
       }


        return binding.root
    }

    private fun login(login: String, password: String) {
        ParseUser.logInInBackground(login, password)
        { _: ParseUser?, parseException: ParseException? ->
            if (parseException != null) {
                Toast.makeText(this@LoginFragment.context, "Ошибка входа", Toast.LENGTH_SHORT)
                    .show()
                return@logInInBackground
            }
            toHomeFragment()
        }
    }

    private fun parseQr(contents: String): Pair<String, String> {
        val (login, password) = contents.split(" ")
        return Pair(login.split(':')[1], password.split(':')[1])
    }

    private fun signIn(data: Pair<String, String>) {
        binding.loginText.setText(data.first)
        binding.passwordText.setText(data.second)
        binding.signIn.performClick()
    }

    private fun toRegistrationFragment() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    private fun toHomeFragment() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_homeFragment)
    }

    private fun createOptions(): ScanOptions {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a qr")
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