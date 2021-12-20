package sch.id.snapan.smarteight.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteight.R
import sch.id.snapan.smarteight.databinding.FragmentLoginBinding
import sch.id.snapan.smarteight.other.EventObserver
import sch.id.snapan.smarteight.ui.auth.AuthViewModel
import sch.id.snapan.smarteight.ui.home.HomeActivity
import sch.id.snapan.smarteight.ui.snackbar
import sch.id.snapan.smarteight.utils.FieldValidators

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToStateObservers()

        binding.btnLogin.setOnClickListener {
            setupTextFieldValidation()
            if (isValidate()) {
                viewModel.loginUser(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        }

        binding.btnRegister.setOnClickListener {
            if (findNavController().previousBackStackEntry != null) {
                findNavController().popBackStack()
            } else findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    private fun subscribeToStateObservers() {
        viewModel.loginStatusUser.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBarLogin.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = { binding.progressBarLogin.visibility = View.VISIBLE }
        ) {
            binding.progressBarLogin.visibility = View.INVISIBLE
            Intent(requireContext(), HomeActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        })
    }

    private fun isValidate() = validateEmail() && validatePassword()

    private fun setupTextFieldValidation() {
        binding.etEmail.addTextChangedListener(TextFieldValidation(binding.etEmail))
        binding.etPassword.addTextChangedListener(TextFieldValidation(binding.etPassword))
    }

    private fun validateEmail(): Boolean {
        if (!FieldValidators.isFormatEmailValid(binding.etEmail.text.toString())) {
            binding.tilEmail.error = getString(R.string.error_et_email_format_wrong)
            return false
        } else {
            binding.tilEmail.error = null
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (binding.etPassword.text.toString().length <= 5) {
            binding.tilPassword.error = getString(R.string.error_et_password_less_than_5)
            return false
        } else {
            binding.tilPassword.error = null
        }
        return true
    }

    inner class TextFieldValidation(private val view: View): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when (view.id) {
                R.id.et_email -> {
                    validateEmail()
                }
                R.id.et_password -> {
                    validatePassword()
                }
            }
        }
        override fun afterTextChanged(p0: Editable?) {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}