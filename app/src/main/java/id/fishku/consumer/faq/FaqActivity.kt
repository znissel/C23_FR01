package id.fishku.consumer.faq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.fishku.consumer.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
    }

    private fun setUpAction(){
        binding.toolbarFaq.setNavigationOnClickListener{ onBackPressedDispatcher.onBackPressed() }
    }
}