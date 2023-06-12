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
import com.codegeniuses.estetikin.ui.authentication.AuthActivity

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
        val tvSignUp = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(500)
        val tvSignUpMessage =
            ObjectAnimator.ofFloat(binding.tvSignupMsg, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                image,
                tvSignUp,
                tvSignUpMessage
            )
            startDelay = 300
        }.start()

        ObjectAnimator.ofFloat(binding.ivSignupIllustration, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.tvSignup, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.tvSignupMsg, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupAction() {
        binding.btnSignUp.setOnClickListener {
            val name = binding.etFullname.text.toString()
            val nickname = binding.etNickname.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            viewModel.register(name, nickname, email, password, confirmPassword)
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
                                val intent = Intent(requireContext(), AuthActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                        }
                    }
                }
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
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
