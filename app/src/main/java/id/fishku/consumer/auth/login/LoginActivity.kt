package id.fishku.consumer.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.EnterNumberActivity
import id.fishku.consumer.core.data.Resource
import id.fishku.consumer.core.data.source.local.datastore.LocalData
import id.fishku.consumer.core.domain.model.User
import id.fishku.consumer.core.domain.params.ParamsToken
import id.fishku.consumer.core.utils.showError
import id.fishku.consumer.core.utils.showMessage
import id.fishku.consumer.databinding.ActivityLoginBinding
import id.fishku.consumer.main.MainActivity
import id.fishku.consumer.otp.SendOTPActivity
import id.fishku.consumer.services.FirebaseService
import id.fishku.consumer.services.GenerateToken
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var saveToLocal: LocalData

    @Inject
    lateinit var getToken: GenerateToken

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("BOSS", "Activity: Login")

        isLoginCheck()
        setupAction()

        getTokenFcm()

        loginResult()
        signGoogleResult()

        binding.btnGoogleSign.setOnClickListener {
            signGoogleAuth()
        }
    }

    private fun isLoginCheck() {
        loginViewModel.getToken().observe(this) { token ->
            if (!token.isNullOrEmpty()) {
                binding.loginContainer.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getTokenFcm() {
        lifecycleScope.launchWhenCreated {
            FirebaseService.sharedPref = saveToLocal
            val token = getToken.getToken()
            saveToLocal.setTokenFcm(token)
            FirebaseService.token = token
        }
    }

    private fun setupAction() {
        binding.apply {
            btnLogin.setOnClickListener { loginHandler() }
            btnRegisterHere.setOnClickListener {
                //view?.findNavController()?.navigate(R.id.action_loginFragment_to_registerFragment)
                startActivity(Intent(this@LoginActivity, EnterNumberActivity::class.java))
            }
            btnForgotPassword.setOnClickListener { "forgot password".showMessage(this@LoginActivity) }
        }
    }

    private fun signGoogleResult() {
        loginViewModel.user.observe(this) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    userLinked(it.data?.email)
                }
                is Resource.Error -> {
                }
            }
        }
    }

    private fun userLinked(emailLink: String?) {
        loginViewModel.userLinked(emailLink!!).observe(this) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    saveToSession(it.data!!)
                }
                is Resource.Error -> {
                    getString(R.string.login_to_linked).showMessage(this)
                    loginViewModel.signOutGoogle(googleSignInClient)
                }
            }
        }
    }

    private fun saveToSession(data: User) {
        loginViewModel.saveSession(data.token!!, data)
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun signGoogleAuth() {
        val signIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signIntent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loginViewModel.signWithGoogle(result.data)
            }
        }

    private fun loginResult() {
        loginViewModel.result.observe(this) {
            when (it) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    val token = it.data?.token
                    val user = it.data?.user
                    val tokenFcm = saveToLocal.getTokenFcm()
                    showLoading(false)
                    if (token != null && user != null) {
                        loginViewModel.initUser(user, tokenFcm)
                        saveToLocalData(user.copy(token = token))
                        toMainMenu()
                        updateToken(user)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    it.message?.showMessage(this)
                }
            }
        }
    }

    private fun updateToken(user: User?) {
        val userEmail = user?.email
        val token = saveToLocal.getTokenFcm()
        if (token != null && userEmail != null) {
            loginViewModel.updateToken(ParamsToken(token, userEmail))
        }
    }

    private fun saveToLocalData(user: User?) {
        saveToLocal.setDataUser(user)
        saveToLocal.setStateAuth(true)
    }

    private fun toMainMenu() {
        val mainIntent = Intent(this, SendOTPActivity::class.java)
        startActivity(mainIntent)
    }

    private fun loginHandler() {
        if (!isFormValid()) return

        val email = binding.edtEmailLogin.text.toString()
        val password = binding.edtPasswordLogin.text.toString()

        loginViewModel.loginUser(email, password)
    }

    private fun isFormValid(): Boolean {
        val email = binding.edtEmailLogin.text.toString()
        val password = binding.edtPasswordLogin.text.toString()

        binding.tilEmailLogin.apply {
            if (email.isEmpty()) {
                showError(true, getString(R.string.email_cannot_be_empty))
            } else {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showError(true, getString(R.string.invalid_email_format))
                } else {
                    showError(false)
                }
            }
        }

        binding.tilPasswordLogin.apply {
            if (password.isEmpty()) {
                showError(true, getString(R.string.password_cannot_be_empty))
            } else if (password.length < 6) {
                showError(true, context.getString(R.string.minimum_password_length))
            } else {
                showError(false)
            }
        }

        return binding.tilEmailLogin.isErrorEnabled == false &&
                binding.tilPasswordLogin.isErrorEnabled == false
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                loadingLogin.visibility = View.VISIBLE
                btnRegisterHere.isClickable = false
                btnForgotPassword.isClickable = false
                btnLogin.isClickable = false
            }
        } else {
            binding.apply {
                loadingLogin.visibility = View.GONE
                btnRegisterHere.isClickable = true
                btnForgotPassword.isClickable = true
                btnLogin.isClickable = true
            }
        }
    }

    /*override fun onDestroy() {
        super.onDestroy()
        binding = null
    }*/
}