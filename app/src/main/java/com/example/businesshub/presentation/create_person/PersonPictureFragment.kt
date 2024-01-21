package com.example.businesshub.presentation.create_person

import android.os.Bundle
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
import com.example.businesshub.databinding.FragmentPersonPictureBinding
import com.example.businesshub.domain.model.User
import kotlinx.coroutines.launch


class PersonPictureFragment : Fragment() {

    val viewmodel: PersonViewModel by activityViewModels()

    var _binding: FragmentPersonPictureBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonPictureBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.finish.setOnClickListener {
            viewmodel.createPerson()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.result.collect {
                    if (it == true) {
                        if (viewmodel.user?.companyId != null) {
                            navigateToHome(viewmodel.user!!)
                        } else {
                            navigateToCompany(viewmodel.user!!)
                        }
                    } else {
                        Toast.makeText(
                            this@PersonPictureFragment.context,
                            "error",
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
        bundle.putString("token", viewmodel.token)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_personPictureFragment_to_companyNameFragment, bundle)
    }

    private fun navigateToHome(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        bundle.putString("token", viewmodel.token)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_personPictureFragment_to_homeFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}