package id.fishku.consumer.welcome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.auth.AuthActivity
import id.fishku.consumer.auth.login.LoginActivity
import id.fishku.consumer.databinding.ActivityWelcomeBinding

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private val welcomeViewModel: WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //activityHandler()
        setupView()

        binding.btnNext.setOnClickListener {
            welcomeViewModel.saveActivity()

            //tambahan
            val intent = Intent(this, LoginActivity::class.java)
            //intent.putExtra("fragment_type", "LOGIN")
            //Log.d("BOSS", "Intent Fragment Type: ${intent.getStringExtra("fragment_type")}")
            Log.d("BOSS", "Welcome :  to Login")
            startActivity(intent)
            finish()

            /*startActivity(Intent(this, AuthActivity::class.java))
            finish()*/
        }
    }

    /*private fun activityHandler() {
        welcomeViewModel.getFirstLaunch().observe(this) {
            if (it == true) {
                binding.root.visibility = View.GONE
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }
    }*/

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}