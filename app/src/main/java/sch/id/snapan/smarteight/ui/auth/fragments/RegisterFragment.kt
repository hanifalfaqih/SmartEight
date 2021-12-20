package sch.id.snapan.smarteight.ui.auth.fragments

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
import sch.id.snapan.smarteight.databinding.FragmentRegisterBinding
import sch.id.snapan.smarteight.other.EventObserver
import sch.id.snapan.smarteight.ui.auth.AuthViewModel
import sch.id.snapan.smarteight.ui.snackbar
import sch.id.snapan.smarteight.utils.FieldValidators

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    private var stringStatus: String?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToRegisterObservers()
        setupTextFieldValidation()

        binding.rgStatus.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.rb_student -> { stringStatus = getString(R.string.student) }
                R.id.rb_teacher -> { stringStatus = getString(R.string.teacher) }
                R.id.rb_employee -> { stringStatus = getString(R.string.employee) }
            }
        }

        binding.btnRegister.setOnClickListener {
            stringStatus?.let { status -> handleRegisterUser(status) }
        }

        binding.btnLogin.setOnClickListener {
            navigateToLoginFragment()
        }
    }

    private fun handleRegisterUser(status: String) {
        if (isValidate()) {
            viewModel.registerUser(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                status
            )
        }
    }

    private fun subscribeToRegisterObservers() {
        viewModel.registerStatusUser.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.progressBarRegister.visibility = View.INVISIBLE
                snackbar(it)
            },
            onLoading = { binding.progressBarRegister.visibility = View.VISIBLE }

        ) {
            binding.progressBarRegister.visibility = View.INVISIBLE
            navigateToLoginFragment()
            snackbar(getString(R.string.registration_user_success))
        })
    }

    private fun navigateToLoginFragment() {
        if (findNavController().previousBackStackEntry != null) {
            findNavController().popBackStack()
        } else findNavController().navigate(
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        )
    }

    private fun isValidate(): Boolean = validateEmail() && validatePassword() && validateConfirmPassword()

    private fun setupTextFieldValidation() {
        binding.etEmail.addTextChangedListener(TextFieldValidation(binding.etEmail))
        binding.etPassword.addTextChangedListener(TextFieldValidation(binding.etPassword))
        binding.etConfirmPassword.addTextChangedListener(TextFieldValidation(binding.etConfirmPassword))
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

    private fun validateConfirmPassword(): Boolean {
        if (binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
            binding.tilConfirmPassword.error = getString(R.string.error_et_confirm_password_not_similar)
            return false
        } else {
            binding.tilConfirmPassword.error = null
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
                R.id.et_confirm_password -> {
                    validateConfirmPassword()
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