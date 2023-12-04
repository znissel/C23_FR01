package id.fishku.consumer.otp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.AuthActivity
import id.fishku.consumer.auth.login.LoginActivity
import id.fishku.consumer.auth.login.LoginViewModel
import id.fishku.consumer.auth.register.RegisterActivity
import id.fishku.consumer.core.data.source.local.datastore.LocalData
import id.fishku.consumer.databinding.ActivityVerifyOtpactivityBinding
import id.fishku.consumer.main.MainActivity
import id.fishku.consumer.utils.Constants.DELAY_SECONDS
import id.fishku.consumer.utils.Constants.RANGE_CODE_SECOND
import id.fishku.consumer.utils.Constants.WAITING_MINUTES
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class VerifyOTPActivity : AppCompatActivity() {

    //private var _binding: ActivityVerifyOtpactivityBinding? = null
    //private val binding get() = _binding!!
    private lateinit var binding: ActivityVerifyOtpactivityBinding
    private val viewModel: SendOtpViewModel by viewModels()

    private val viewModelLogin: LoginViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    @Inject
    lateinit var saveToLocal: LocalData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpactivityBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        Log.d("BOSS", "Activity: VerifyOTP")

        binding.btnVerify.setOnClickListener {
            loadCheckCode()
        }
        binding.tvResend.setOnClickListener {
            resendSmsOtp()
            //pakai seleksi kondisi buat WA/SMS?
            startGenerateOtp()
            textVisibility(false)
        }
        binding.btnBack.setOnClickListener {
            //back()
            onBackPressedDispatcher.onBackPressed()
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
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("BOSS", "signInWithCredential:success")
                    val state = saveToLocal.getStateAuth()
                    val user = saveToLocal.getDataUser()

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
                    Log.d("BOSS", "signInWithCredential:failure", task.exception)
                    /*if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // invalid
                    }*/
                }
            }
    }

    private fun toRegisterFragment() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun resendSmsOtp() {
        //progress bar visible
        val user = saveToLocal.getDataUser()
        Log.d("BOSS", "OTP - number: ${user}")
        val number = "+" + user.phoneNumber.toString()
        Log.d("BOSS", "OTP - number: ${number}")

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    //progress bar gone
                    Log.d("BOSS", "onVerificationCompleted:$p0")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    //progress bar gone
                    Log.w("BOSS", "onVerificationFailed", p0)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    //progress bar gone
                    super.onCodeSent(p0, p1)
                    saveToLocal.setVerificationId(p0)
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun resendWaOtp() {
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

    /*private fun back() {
        val intent = Intent(this, SendOTPActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }*/

    /*override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }*/
}