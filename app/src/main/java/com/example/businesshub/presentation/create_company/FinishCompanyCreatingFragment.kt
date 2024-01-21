package com.example.businesshub.presentation.create_company

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
import com.example.businesshub.databinding.FragmentFinishCompanyCreatingBinding
import com.example.businesshub.presentation.HomeFragment
import kotlinx.coroutines.launch


class FinishCompanyCreatingFragment : Fragment() {

    private var _binding: FragmentFinishCompanyCreatingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateCompanyViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishCompanyCreatingBinding.inflate(inflater, container, false)
        drawData()

        binding.finish.setOnClickListener {
            setProgressBar(true)
            viewModel.createCompany()
        }

        binding.back.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect { created ->
                    if (created == true) {
                        toHomeFragment()
                        return@collect
                    }
                    if (created == false) {
                        Toast.makeText(
                            this@FinishCompanyCreatingFragment.context,
                            "Ошибка создания компании",
                            Toast.LENGTH_SHORT
                        ).show()
                        setProgressBar(false)
                    }
                }
            }
        }
        return binding.root
    }

    private fun toHomeFragment() {
        val bundle = Bundle()
        bundle.putParcelable("user", viewModel.user)
        bundle.putString("token", viewModel.token)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_finishCompanyCreatingFragment_to_homeFragment, bundle)
    }

    private fun drawData() {
        binding.name.text = viewModel.name.value
        binding.desc.text = viewModel.desc.value
        binding.addr.text = viewModel.addr.value
        binding.inn.text = viewModel.inn.value
        binding.kpp.text = viewModel.kpp.value
        binding.ogrn.text = viewModel.ogrn.value
    }

    private fun setProgressBar(isEnabled: Boolean) {
        binding.progress.visibility = if (isEnabled) View.VISIBLE else View.GONE
        binding.layout.visibility = if (!isEnabled) View.VISIBLE else View.GONE
    }
}