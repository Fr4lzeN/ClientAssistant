package com.example.businesshub.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentSplashScreenBinding
import com.parse.ParseUser
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)

        binding.text.alpha = 0f
        scaleView(binding.stars, 0f, 1f, 1000)

        viewLifecycleOwner.lifecycleScope.launch{

            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isSigned.collect{ isSigned ->
                    if (isSigned==true){

                        viewModel.userData.collect {
                            val bundle = Bundle()
                            bundle.putParcelable("user", it)
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_splashScreenFragment_to_homeFragment, bundle)
                        }
                    }
                    if (isSigned==false){
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_splashScreenFragment_to_loginFragment)
                    }
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
                //checkUser(getCurrentUser())
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        v.animate()

    }

    private fun checkUser(user: ParseUser?) {
        if (user != null) {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_splashScreenFragment_to_homeFragment)
        } else {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }
    }

    private fun getCurrentUser(): ParseUser? {

        return ParseUser.getCurrentUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}