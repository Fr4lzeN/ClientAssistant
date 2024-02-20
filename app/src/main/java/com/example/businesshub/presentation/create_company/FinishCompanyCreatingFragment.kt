package com.example.businesshub.presentation.create_company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentFinishCompanyCreatingBinding
import com.example.businesshub.presentation.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FinishCompanyCreatingFragment : Fragment() {

    private var _binding: FragmentFinishCompanyCreatingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateCompanyViewModel by viewModels()


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
                viewModel.result
                    .filterNotNull()
                    .collect { created ->
                        if (created) {
                            toHomeFragment()
                            return@collect
                        } else {
                            viewModel.clearState()
                            Toast.makeText(
                                this@FinishCompanyCreatingFragment.context,
                                resources.getResourceName(R.string.company_error),
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
        val fm = parentFragmentManager
        while (fm.backStackEntryCount > 1) {
            fm.popBackStack()
        }
        val ft = fm.beginTransaction()
        val bundle = Bundle()
        bundle.putParcelable("user", viewModel.user)
        bundle.putString("token", viewModel.token)
        val fragment = HomeFragment()
        fragment.arguments = bundle
        ft.replace(R.id.nav_host_fragment, fragment).commit()
    }

    private fun drawData() {
        binding.name.text = viewModel.name.value
        binding.desc.text = viewModel.desc.value
        binding.addr.text = viewModel.addr.value
        binding.inn.text = viewModel.inn.value
        binding.kpp.text = viewModel.kpp.value
        binding.ogrn.text = viewModel.ogrn.value
    }

    private fun setProgressBar(b: Boolean) {
        binding.layout.visibility = if (b) View.GONE else View.VISIBLE
        binding.progressCircular.visibility = if (b) View.VISIBLE else View.GONE
    }
}