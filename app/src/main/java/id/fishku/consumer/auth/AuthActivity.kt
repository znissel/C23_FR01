package id.fishku.consumer.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.login.LoginFragment
import id.fishku.consumer.auth.register.RegisterFragment

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (intent.hasExtra("FRAGMENT_TYPE")) {
            val fragmentType = intent.getStringExtra("FRAGMENT_TYPE")
            if (fragmentType == "LOGIN") {
                // Tampilkan LoginFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_auth, LoginFragment())
                    .commit()
            } else if (fragmentType == "REGISTER") {
                // Tampilkan RegisterFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_auth, RegisterFragment())
                    .commit()
            }
        }

        /*if (intent.getBooleanExtra(OPEN_REGISTER_FRAGMENT, true)) {
            Log.d("BOSS", "Auth : ini yang intent, register")
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_auth, RegisterFragment())
                .commit()
        } else {
            Log.d("BOSS", "Auth : ini yang login")
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_auth, LoginFragment())
                .commit()
        }*/
    }

    companion object {
        val OPEN_REGISTER_FRAGMENT = "open_register_fragment"
    }
}