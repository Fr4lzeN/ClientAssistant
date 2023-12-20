package com.example.businesshub.presentation.authorization

import android.os.Bundle
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
import com.example.businesshub.databinding.FragmentRegistrationBinding
import com.example.businesshub.domain.model.User
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
            val (id,password,passwordConfirm,email) = getData()
            if (!checkPassword(password, passwordConfirm)) {
                Toast.makeText(this.activity, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signUp(id,password,email)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isSigned.collect{ isSigned->
                    if (isSigned==true){
                        viewModel.userData.collect{ toHomeFragment(it!!) }
                    }else{
                        Toast.makeText(this@RegistrationFragment.context,"Ошибка входа",Toast.LENGTH_SHORT).show()
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

    private fun toHomeFragment(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user",user)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_registrationFragment_to_homeFragment, bundle)
    }


    private  fun getData():Array<String>{
        return arrayOf(
            binding.id.text.toString(),
            binding.password.text.toString(),
            binding.passwordConfirm.text.toString(),
            binding.email.text.toString()
        )
    }


}