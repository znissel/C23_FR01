package id.fishku.consumer.otp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.AuthActivity
import id.fishku.consumer.auth.AuthActivity.Companion.OPEN_REGISTER_FRAGMENT
import id.fishku.consumer.auth.login.LoginViewModel
import id.fishku.consumer.core.data.source.local.datastore.LocalData
import id.fishku.consumer.core.data.source.remote.request.Component
import id.fishku.consumer.core.data.source.remote.request.OtpRequest
import id.fishku.consumer.core.data.source.remote.request.Parameter
import id.fishku.consumer.core.data.source.remote.request.Template
import id.fishku.consumer.databinding.ActivityVerifyOtpactivityBinding
import id.fishku.consumer.main.MainActivity
import id.fishku.consumer.utils.Constants.DELAY_SECONDS
import id.fishku.consumer.utils.Constants.RANGE_CODE_SECOND
import id.fishku.consumer.utils.Constants.WAITING_MINUTES
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VerifyOTPActivity : AppCompatActivity() {

    private var _binding: ActivityVerifyOtpactivityBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SendOtpViewModel by viewModels()
    //private val viewModelLogin: LoginViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    @Inject
    lateinit var saveToLocal: LocalData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVerifyOtpactivityBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            loadCheckCode()
        }
        binding.tvResend.setOnClickListener {
            resendOtp()
            startGenerateOtp()
            textVisibility(false)
        }
        binding.btnBack.setOnClickListener {
            back()
        }
        init()
        startGenerateOtp()
        otpResponse()
    }

    private fun checkVerificationCode() {
        val verifyCode = binding.edtVerifyCode.text.trim().toString()
        val verificationId = saveToLocal.getVerificationId().toString()
        if (verifyCode.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, verifyCode)
            signInWithPhoneAuthCredential(credential)
            /*if (verifyCode == localCode) {
                if (state) {
                    viewModelLogin.saveSession(user.token.toString(), user)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                    startActivity(intent)
                } else {
                    toRegisterFragment()
                }
            } else {
                binding.tvError.visibility = View.VISIBLE
            }*/
        }
        /*val verifyCode = binding.edtVerifyCode.text.trim().toString()
        val localCode = saveToLocal.getCodeOtp()
        val state = saveToLocal.getStateAuth()
        val user = saveToLocal.getDataUser()
        if (verifyCode.isNotEmpty()) {
            val codeWa = verifyCode.toInt()
            if (codeWa == localCode) {
                if (state) {
                    viewModelLogin.saveSession(user.token.toString(), user)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                    startActivity(intent)
                } else {
                    toRegisterFragment()
                }
            } else {
                binding.tvError.visibility = View.VISIBLE
            }
        }*/
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("BOSS", "signInWithCredential:success")
                    val user = task.result?.user
                    toRegisterFragment()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("BOSS", "signInWithCredential:failure", task.exception)
                    /*if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }*/
                    // Update UI
                }
            }
    }

    private fun toRegisterFragment() {
        //cara 1
        /*val pendingIntent = NavDeepLinkBuilder(this.applicationContext)
            .setGraph(R.navigation.auth_navigation)
            .setDestination(R.id.registrationFragment)
            .createPendingIntent()

        try {
            pendingIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }*/

        //cara 2
        val intent = Intent(this, AuthActivity::class.java)
        intent.putExtra("FRAGMENT_TYPE", "REGISTER")
        startActivity(intent)
        /*val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(OPEN_REGISTER_FRAGMENT, true)
        startActivity(intent)
        finish()*/

        //cara 3
        /*val resultIntent = Intent()
        resultIntent.putExtra("open_register_fragment", true)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()*/

        //cara 4
        /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_auth) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.action_loginFragment_to_registerFragment)*/
    }

    private fun resendOtp() {
        /*val codeOtp = saveToLocal.getCodeOtp()
        val user = saveToLocal.getDataUser()
        val components = Component(
            parameters = listOf(
                Parameter(
                    text = "$codeOtp"
                )
            )
        )
        val template = Template(
            components = listOf(
                components
            )
        )
        val otpRequest = OtpRequest(
            to = "${user.phoneNumber}",
            template = template
        )
        viewModel.sendOtpCode(otpRequest)*/
    }

    private fun init() {
        binding.tvNum.text = saveToLocal.getNumber()
    }

    private fun otpResponse() {
        viewModel.otpResponse.observe(this) { res ->
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun startGenerateOtp() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                var count = WAITING_MINUTES
                while (count > 0) {
                    count -= 1
                    delay(DELAY_SECONDS)
                    binding.tvCount.text =
                        resources.getString(R.string.waiting_code, count.toString())
                    if (count == 0) {
                        val random = getRandomNum()
                        saveOtpCode(random)
                    }
                }
            }
        }
    }

    private fun loadCheckCode() {
        lifecycleScope.launch {
            isLoading(true)
            delay(DELAY_SECONDS)
            checkVerificationCode()
            isLoading(false)
        }
    }

    private fun getRandomNum(): Int {
        return (RANGE_CODE_SECOND).shuffled().first()
    }

    private fun saveOtpCode(code: Int) {
        saveToLocal.setCodeOtp(code)
        textVisibility(true)
    }

    private fun textVisibility(value: Boolean = false) {
        if (value) {
            binding.tvResend.visibility = View.VISIBLE
            binding.tvCount.visibility = View.GONE
        } else {
            binding.tvCount.visibility = View.VISIBLE
            binding.tvResend.visibility = View.GONE
        }
    }

    private fun isLoading(value: Boolean) {
        if (value)
            binding.loading.visibility = View.VISIBLE
        else
            binding.loading.visibility = View.GONE
    }

    private fun back() {
        val intent = Intent(this, SendOTPActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}