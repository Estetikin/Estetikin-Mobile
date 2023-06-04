package com.codegeniuses.estetikin.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentSignUpBinding
import com.codegeniuses.estetikin.factory.ViewModelFactory
import com.codegeniuses.estetikin.helper.LoadingHandler
import com.codegeniuses.estetikin.model.result.Result
import com.codegeniuses.estetikin.ui.MainActivity

class SignUpFragment : Fragment(), LoadingHandler {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ViewModelFactory
    private val viewModel: SignUpViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        playAnimation()
        setupAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun playAnimation() {
        val image =
            ObjectAnimator.ofFloat(binding.ivSignupIllustration, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.etFullname, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(500)
        val confirmPassword =
            ObjectAnimator.ofFloat(binding.etConfirmPassword, View.ALPHA, 1f).setDuration(500)
        val signUpButton =
            ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)
        val loginWith =
            ObjectAnimator.ofFloat(binding.llSignupWith, View.ALPHA, 1f).setDuration(500)
        val googleLogo =
            ObjectAnimator.ofFloat(binding.llGoogleLogo, View.ALPHA, 1f).setDuration(500)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                image,
                name,
                email,
                password,
                confirmPassword,
                signUpButton,
                loginWith,
                googleLogo,
                btnRegister
            )
            startDelay = 500
        }.start()

        binding.ivSignupIllustration.animate().apply {
            duration = 1000
            alpha(.5f)
            rotationYBy(360f)
            translationYBy(200f)
        }.withEndAction {
            binding.ivSignupIllustration.animate().apply {
                duration = 1000
                alpha(1f)
                rotationXBy(360f)
                translationYBy(-200f)
            }
        }.start()

        ObjectAnimator.ofFloat(binding.ivSignupIllustration, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupAction() {
        binding.btnSignUp.setOnClickListener {
            val name = binding.etFullname.text.toString()
            val email = binding.etFullname.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            viewModel.register(name, email, password, confirmPassword)
                .observe(viewLifecycleOwner) {
                    it?.let { result ->
                        when (result) {
                            is Result.Loading -> {
                                loadingHandler(true)
                            }
                            is Result.Error -> {
                                loadingHandler(false)
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to Register",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            is Result.Success -> {
                                loadingHandler(false)
                                Toast.makeText(
                                    requireContext(),
                                    "Register Success!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                        }
                    }
                }
            binding.btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(requireContext())
    }

    override fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingAnimation.visibility = View.VISIBLE
        } else {
            binding.loadingAnimation.visibility = View.GONE
        }
    }
}
