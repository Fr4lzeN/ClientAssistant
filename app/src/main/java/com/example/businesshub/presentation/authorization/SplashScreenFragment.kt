package com.example.businesshub.presentation.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.businesshub.R
import com.example.businesshub.databinding.FragmentSplashScreenBinding
import com.example.businesshub.domain.model.User
import com.example.businesshub.presentation.HomeFragment
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
                            if (it!!.companyId!=null){
                                navigateToHome(it)
                            }else{
                                navigateToCompany(it)
                            }

                        }
                    }
                    if (isSigned==false){
                       navigateToAuthorization()
                    }
                }
            }

        }

        return binding.root
    }

    private fun navigateToCompany(user: User) {
        val bundle = Bundle()
        bundle.putParcelable("user",user)
        bundle.putString("token", viewModel.token.value!!)
        Navigation.findNavController(binding.root).navigate(R.id.action_splashScreenFragment_to_companyNameFragment, bundle)
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

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        v.animate()

    }

    private fun navigateToAuthorization(){
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_splashScreenFragment_to_loginFragment)
    }

    private fun navigateToHome(user: User){
        val bundle = Bundle()
        bundle.putParcelable("user", user)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_splashScreenFragment_to_homeFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}