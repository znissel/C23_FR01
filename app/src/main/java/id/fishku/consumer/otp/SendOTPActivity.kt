package id.fishku.consumer.otp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.AuthActivity
import id.fishku.consumer.core.data.Resource
import id.fishku.consumer.core.data.source.local.datastore.LocalData
import id.fishku.consumer.core.data.source.remote.request.Component
import id.fishku.consumer.core.data.source.remote.request.OtpRequest
import id.fishku.consumer.core.data.source.remote.request.Parameter
import id.fishku.consumer.core.data.source.remote.request.Template
import id.fishku.consumer.databinding.ActivitySendOtpactivityBinding
import id.fishku.consumer.main.MainActivity
import id.fishku.consumer.utils.Constants.RANGE_CODE_FIRST
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SendOTPActivity : AppCompatActivity() {

    private var _binding: ActivitySendOtpactivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SendOtpViewModel by viewModels()

    private val auth = FirebaseAuth.getInstance()

    @Inject
    lateinit var saveToLocal: LocalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySendOtpactivityBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        //percobaan
        //val data = HashMap<String, String>()
        //val db = FirebaseFirestore.getInstance()
        //db.collection("test").add(data);

        init()
        otpResponse()
        //startGenerateOtp()

        binding.waCard.setOnClickListener {
            sendWaOtp()
            Log.d("BOSS", "udah keklik kok (wa)")
        }
        binding.smsCard.setOnClickListener {
            sendSmsOtp()
            Log.d("BOSS", "udah keklik kok (sms)")
        }
        binding.btnBack.setOnClickListener {
            back()
        }
    }

    private fun sendSmsOtp() {
        //progress bar visible
        val codeOtp = saveToLocal.getCodeOtp().toString()
        Log.d("BOSS", "OTP - number: ${codeOtp}")
        val user = saveToLocal.getDataUser()
        Log.d("BOSS", "OTP - number: ${user}")
        val number = "+" + user.phoneNumber.toString()
        Log.d("BOSS", "OTP - number: ${number}")

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this) //klu diubah ke VerifyOTPActivity?
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    //progress bar gone
                    Log.d("BOSS", "OTP SMS: Completed")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    //progress bar gone
                    Log.d("BOSS", "OTP SMS: Gagal")
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    //progress bar gone
                    super.onCodeSent(p0, p1)
                    saveToLocal.setVerificationId(p0)
                    Log.d("BOSS", "OTP SMS: Terkirim")
                    Log.d("BOSS", "OTP SMS: p0 = ${p0}")
                    Log.d("BOSS", "OTP SMS: p1 = ${p1}")
                    startToVerifyOTP()
                }

            }).build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            PhoneAuthProvider.verifyPhoneNumber(options)
            Log.d("BOSS", "OTP SMS - options : ${options}")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                100
            )
        }
    }

    private fun init() {
        val number = saveToLocal.getNumber()
        number?.let {
            if (number[0] == '0') {
                binding.tvNumber.text = number.replaceRange(0, 1, "62")
            } else {
                binding.tvNumber.text = number
            }
        }
    }

    private fun sendWaOtp() {
        /*val codeOtp = saveToLocal.getCodeOtp()
        Log.d("BOSS", "OTP - kode: ${codeOtp}")
        val user = saveToLocal.getDataUser()
        Log.d("BOSS", "OTP - user: ${user}")
        val number = user.phoneNumber
        Log.d("BOSS", "OTP - number: ${number}")
        val components = Component(parameters = listOf(Parameter(text = "$codeOtp")))
        val template = Template(components = listOf(components))
        if (number != null) {
            val otpRequest = OtpRequest(to =
            if (number[0] == '0')
                number.replaceRange(0, 1, "62")
            else number, template = template
            )
            Log.d("BOSS", "OTP - otpRequest: ${otpRequest}")
            viewModel.sendOtpCode(otpRequest)
        }*/
    }

    private fun otpResponse() {
        viewModel.otpResponse.observe(this) { res ->
            when (res) {
                is Resource.Loading -> {
                    isLoading(true)
                    Log.d("BOSS", "OTP: Loading")
                }

                is Resource.Error -> {
                    isLoading(false)
                    Log.d("BOSS", "OTP: Error")
                }

                is Resource.Success -> {
                    isLoading(false)
                    Log.d("BOSS", "OTP: Berhasil, start ke verify...")
                    startToVerifyOTP()
                }

                else -> {}
            }
        }
    }

    private fun startToVerifyOTP() {
        //progress bar visible
        val user = saveToLocal.getDataUser()
        if (user.phoneNumber != null) {
            val intent = Intent(this, VerifyOTPActivity::class.java)
            //progress bar gone
            startActivity(intent)
        }
    }

    private fun startGenerateOtp() {
        lifecycleScope.launch {
            val random = getRandomNum()
            saveOtpCode(random)
        }
    }

    private fun getRandomNum(): Int {
        return (RANGE_CODE_FIRST).shuffled().first()
    }

    private fun saveOtpCode(code: Int) {
        saveToLocal.setCodeOtp(code)
    }

    private fun isLoading(value: Boolean) {
        if (value)
            binding.loading.visibility = View.VISIBLE
        else
            binding.loading.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //klik back ke auth atau ke enter number?
    private fun back() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}