package id.fishku.consumer.fishrecipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.fishku.consumer.R
import id.fishku.consumer.core.ui.FishTypeAdapter
import id.fishku.consumer.databinding.ActivityFishRecipeBinding

class FishRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishRecipeBinding

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideActionBar()
        setUpAction()
    }

    private fun setUpAction() {
        binding.toolbarRecipe.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}