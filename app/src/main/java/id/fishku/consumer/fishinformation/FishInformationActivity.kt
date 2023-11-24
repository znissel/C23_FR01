package id.fishku.consumer.fishinformation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.fishku.consumer.R
import id.fishku.consumer.databinding.ActivityFishInformationBinding

class FishInformationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFishInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideActionBar()
        setUpAction()
    }

    private fun setUpAction() {
        binding.toolbarInformation.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}