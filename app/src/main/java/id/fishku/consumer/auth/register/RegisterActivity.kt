package id.fishku.consumer.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.EnterNumberActivity
import id.fishku.consumer.auth.login.LoginActivity
import id.fishku.consumer.core.data.Resource
import id.fishku.consumer.core.data.source.local.datastore.LocalData
import id.fishku.consumer.core.utils.showError
import id.fishku.consumer.core.utils.showMessage
import id.fishku.consumer.databinding.ActivityRegisterBinding
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var saveToLocal: LocalData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("BOSS", "Activity: Register")

        setupAction()
        registerResult()
    }

    private fun setupAction() {
        val nomor = saveToLocal.getNumber()
        binding.edtPhoneRegister.setText(nomor)
        binding.apply {
            btnRegister.setOnClickListener {
                val inputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager

                inputMethodManager.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                registerHandler()
            }
            btnLoginHere.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    private fun registerResult() {
        registerViewModel.result.observe(this) {
            when (it) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    it.data?.message?.showMessage(this)
                    /*binding.root.findNavController()
                        .navigate(R.id.action_registerFragment_to_loginFragment)*/
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
                is Resource.Error -> {
                    showLoading(false)
                    it.message?.showMessage(this)
                }
            }
        }
    }

    private fun registerHandler() {
        if (!isFormValid()) return

        val name = binding.edtNameRegister.text.toString()
        val email = binding.edtEmailRegister.text.toString()
        val password = binding.edtPasswordRegister.text.toString()
        val phoneNumber = binding.edtPhoneRegister.text.toString()

        registerViewModel.registerUser(name, email, password, phoneNumber)
    }

    private fun isFormValid(): Boolean {
        val name = binding.edtNameRegister.text.toString()
        val email = binding.edtEmailRegister.text.toString()
        val password = binding.edtPasswordRegister.text.toString()
        val phoneNumber = binding.edtPhoneRegister.text.toString()

        binding.tilNameRegister.apply {
            if (name.isEmpty()) {
                showError(true, context.getString(R.string.name_cannot_be_empty))
            } else {
                showError(false)
            }
        }

        binding.tilEmailRegister.apply {
            if (email.isEmpty()) {
                showError(true, getString(R.string.email_cannot_be_empty))
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showError(true, getString(R.string.invalid_email_format))
                } else {
                    showError(false)
                }
            }
        }

        binding.tilPasswordRegister.apply {
            if (password.isEmpty()) {
                showError(true, getString(R.string.password_cannot_be_empty))
            } else {
                showError(false)
            }
        }

        binding.tilPhoneRegister.apply {
            if (phoneNumber.isEmpty()) {
                showError(true, getString(R.string.phone_cannot_be_empty))
            } else {
                if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                    showError(true, getString(R.string.invalid_phone_format))
                } else {
                    showError(false)
                }
            }
        }

        return binding.tilNameRegister.isErrorEnabled == false &&
                binding.tilEmailRegister.isErrorEnabled == false &&
                binding.tilPasswordRegister.isErrorEnabled == false &&
                binding.tilPhoneRegister.isErrorEnabled == false
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                loadingRegister.visibility = View.VISIBLE
                btnLoginHere.isClickable = false
                btnRegister.isClickable = false
            }
        } else {
            binding.apply {
                loadingRegister.visibility = View.GONE
                btnLoginHere.isClickable = true
                btnRegister.isClickable = true
            }
        }
    }

    /*override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }*/
}