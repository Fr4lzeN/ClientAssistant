package com.example.businesshub.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.businesshub.databinding.FragmentHomeBinding
import com.example.businesshub.databinding.FragmentLoginBinding
import com.parse.ParseUser
import kotlin.system.exitProcess

class HomeFragment : Fragment() {

    private var _binding:  FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var isOpen = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.bar.setOnClickListener{
            if (!isOpen){
                binding.drawer.open()
            }else{
                binding.drawer.close()
            }
            isOpen = !isOpen
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}