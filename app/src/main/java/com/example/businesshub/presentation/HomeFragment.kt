package com.example.businesshub.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.businesshub.databinding.FragmentHomeBinding
import com.example.businesshub.domain.model.User
import com.example.businesshub.presentation.authorization.SignInViewModel

class HomeFragment : Fragment() {

    private var _binding:  FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by activityViewModels()
    var isOpen = false



    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val user: User = arguments?.getParcelable("user")!!
        binding.userName.text=user.username

        binding.delete.setOnClickListener{
            viewModel.logOut(user)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
