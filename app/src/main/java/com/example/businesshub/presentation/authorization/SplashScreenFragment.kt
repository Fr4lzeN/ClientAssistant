package com.example.businesshub.presentation.authorization

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.businesshub.R
import com.example.businesshub.core.AuthState
import com.example.businesshub.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph_xml) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        binding.text.alpha = 0f
        scaleView(binding.stars, 0f, 1f, 1000)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exceptionState
                    .collect {
                        Toast
                            .makeText(this@SplashScreenFragment.context, it, Toast.LENGTH_SHORT)
                            .show()
                        navigateToAuthorization()
                    }
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }

    private fun scaleView(v: View, startScale: Float, endScale: Float, time: Long = 1500) {
        val animation = ScaleAnimation(
            startScale, endScale, startScale, endScale,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        )
        animation.fillAfter = true
        animation.duration = time
        v.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.text.alpha = 1f
                alphaView(binding.text)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        v.startAnimation(animation)
    }

    fun alphaView(v: View) {
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 1000
        animation.fillAfter = true
        v.animation = animation
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d("user", "animation end")
                collectAuthState()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        v.animate()
    }

    private fun collectAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState
                    .filterNotNull()
                    .collect {
                        Log.d("user", it.toString())
                        when (it) {
                            AuthState.SUCCESS -> navigateToHome()
                            AuthState.MINIMAL_SUCCESS -> navigateToPerson()
                            AuthState.FAILURE -> navigateToAuthorization()
                        }
                    }
            }
        }
    }

    private fun navigateToAuthorization() {
        Log.d("user", "navigation")
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_splashScreenFragment_to_signInFragment)
    }

    private fun navigateToHome() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_splashScreenFragment_to_homeFragment)
    }

    private fun navigateToPerson() {
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_splashScreenFragment_to_personInfoFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}