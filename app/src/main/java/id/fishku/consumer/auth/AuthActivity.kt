package id.fishku.consumer.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.register.RegisterFragment

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (intent.getBooleanExtra("open_register_fragment", false)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_auth, RegisterFragment())
                .commit()
        } else {
            //TODO
        }
    }
}