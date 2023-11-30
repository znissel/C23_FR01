package id.fishku.consumer.otp

import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.auth.AuthActivity
import id.fishku.consumer.core.data.Resource
import id.fishku.consumer.core.data.source.local.datastore.LocalData
import id.fishku.consumer.core.data.source.remote.request.Component
import id.fishku.consumer.core.data.source.remote.request.OtpRequest
import id.fishku.consumer.core.data.source.remote.request.Parameter
import id.fishku.consumer.core.data.source.remote.request.Template
import id.fishku.consumer.databinding.ActivitySendOtpactivityBinding
import id.fishku.consumer.utils.Constants.RANGE_CODE_FIRST
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Send o t p activity
 *
 * Page for send otp via WhatsApp
 */
@AndroidEntryPoint
class SendOTPActivity : AppCompatActivity() {

    private var _binding: ActivitySendOtpactivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SendOtpViewModel by viewModels()

    @Inject
    lateinit var saveToLocal: LocalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySendOtpactivityBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        //percobaan, initializeApp ga harus keknya, masih aman
        //FirebaseApp.initializeApp(this)

        //val data = HashMap<String, String>()
        /*data["Field1"] = "nyoba"
        val db = FirebaseFirestore.getInstance()

        db.collection("test")
            .add(data)
            .addOnSuccessListener { documentReference ->
            println("Data berhasil ditambahkan dengan ID: ${documentReference.id}")
            Log.d("BOSS", "Input: berhasil")
            }
            .addOnFailureListener { e ->
            // Jika penambahan gagal
            println("Gagal menambahkan data: $e")
                Log.d("BOSS", "Input: gagal")
        }*/
        //FirebaseFirestore.getInstance().collection("test").add(data);

        init()
        otpResponse()
        startGenerateOtp()

        binding.waCard.setOnClickListener {
            sendOtp()
            /*val user = saveToLocal.getDataUser()
            val number = user.phoneNumber
            sendOtp(number)*/
            Log.d("BOSS", "udah keklik kok")
        }
        binding.btnBack.setOnClickListener {
            back()
        }
    }

    /**
     * Init
     * Show element in first open
     */
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

    //tambahan
    /*val db = FirebaseAuth.getInstance()
    private var verificationCode: String? = null
    private fun sendOtp(phoneNumber: String?) {
        setInProgress(true)

        val builder = phoneNumber?.let {
            PhoneAuthOptions.newBuilder(db)
                .setPhoneNumber(it)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        //signIn(phoneAuthCredential)
                        Log.d("BOSS", "KEKIRIM")
                        setInProgress(false)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Log.d("BOSS", "GA KEKIRIM")
                        setInProgress(false)
                    }

                    override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(s, forceResendingToken)
                        Log.d("BOSS", "GA TAHU")
                        verificationCode = s
                        setInProgress(false)

                        sendSms(phoneNumber, "Your OTP is: $verificationCode")
                    }
                })
        }
    }

    private fun sendSms(phoneNumber: String?, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.loading.visibility = View.VISIBLE
            binding.waCard.visibility = View.GONE
        } else {
            binding.loading.visibility = View.GONE
            binding.waCard.visibility = View.VISIBLE
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        setInProgress(true)

        db.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(this) { task ->
                setInProgress(false)
                if (task.isSuccessful) {
                    val intent = Intent(this, LoginUsernameActivity::class.java)
                    intent.putExtra("phone", phoneNumber)
                    startActivity(intent)
                } else {
                    showToast("OTP verification failed")
                }
            }
    }*/

    private fun sendOtp() {
        val codeOtp = saveToLocal.getCodeOtp()
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
        }
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
            }
        }
    }

    private fun startToVerifyOTP() {
        val user = saveToLocal.getDataUser()
        if (user.phoneNumber != null) {
            val intent = Intent(this, VerifyOTPActivity::class.java)
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