package com.example.businesshub.presentation.companies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentHomeBinding
import com.example.businesshub.presentation.authorization.AuthViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph_xml)
    private val viewModel: CompanyViewModel by viewModels()
    private var adapter: CompanyListAdapter? = null
    var isOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.userName.text =
            "${Firebase.auth.currentUser?.uid} + ${Firebase.auth.currentUser?.email}"
        binding.recycler.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        binding.delete.setOnClickListener {
            authViewModel.logOut()
        }
        binding.add.setOnClickListener {
            navigateToCompanyCreation()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.companyList
                    .filterNotNull()
                    .collect {
                        Log.d("company", "collected ${it.size}")
                        if (adapter == null) {
                            adapter = CompanyListAdapter(it)
                            binding.recycler.adapter = adapter
                        } else {
                            adapter?.submitList(it)
                        }
                    }
            }
        }

        return binding.root
    }

    private fun navigateToCompanyCreation() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_homeFragment_to_companyNameFragment)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCompanyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
