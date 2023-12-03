package id.fishku.consumer.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.fishku.consumer.R
import id.fishku.consumer.auth.login.LoginFragment
import id.fishku.consumer.auth.register.RegisterFragment
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    /*override fun onRestart() {
        super.onRestart()

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_auth)?.findNavController()
        navController?.navigate(R.id.action_loginFragment_to_registerFragment)
        //findNavController(0).navigate(R.id.action_loginFragment_to_registerFragment)
    }*/

    /*override fun onResume() {
        super.onResume()

        val fragmentType = intent.getStringExtra(FRAGMENT_TYPE)
        Log.d("BOSS", "Auth : fragment = $fragmentType")

        if (fragmentType == "REGISTER") {
            // Tampilkan LoginFragment
            Log.d("BOSS", "Auth : nav = register")
            loadFragment(RegisterFragment())
        } else if (fragmentType == "LOGIN") {
            // Tampilkan RegisterFragment
            Log.d("BOSS", "Auth : nav = login")
            loadFragment(LoginFragment())
        }
    }*/

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_auth, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val FRAGMENT_TYPE = "fragment_type"
    }
}