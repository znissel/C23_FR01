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

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var verificationId: String

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
        //startGenerateOtp()

        binding.waCard.setOnClickListener {
            sendWaOtp()
            /*val user = saveToLocal.getDataUser()
            val number = user.phoneNumber
            sendOtp(number)*/
            Log.d("BOSS", "udah keklik kok (wa)")
        }
        binding.smsCard.setOnClickListener {
            sendSmsOtp()
            /*val user = saveToLocal.getDataUser()
            val number = user.phoneNumber
            sendOtp(number)*/
            Log.d("BOSS", "udah keklik kok (sms)")
        }
        binding.btnBack.setOnClickListener {
            back()
        }
    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    private fun sendSmsOtp() {
        val codeOtp = saveToLocal.getCodeOtp().toString()
        Log.d("BOSS", "OTP - number: ${codeOtp}")
        val user = saveToLocal.getDataUser()
        Log.d("BOSS", "OTP - number: ${user}")
        val number = "+" + user.phoneNumber.toString()
        Log.d("BOSS", "OTP - number: ${number}")

        /*val smsManager: SmsManager = SmsManager.getDefault()
        //ubah stringnya
        val parts = smsManager.divideMessage(getString(R.string.otp_msg) + codeOtp)
        val phoneNumber = number*/

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("BOSS", "OTP SMS: Completed")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("BOSS", "OTP SMS: Gagal")
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    verificationId = p0

                }

            }).build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
            PhoneAuthProvider.verifyPhoneNumber(options)
            Log.d("BOSS", "OTP SMS - options : ${options}")
            /*val credential = PhoneAuthProvider.getCredential(verificationId, codeOtp)
            auth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        //startActivity(Intent(this, MainActivity::class.java)) hrsnya ke fragment?
                        //finish()
                        Log.d("BOSS", "OTP SMS : Keknya ini berhasil sign in")
                    } else {
                        Log.d("BOSS", "OTP SMS : Gagal sign in")
                    }
                }*/
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