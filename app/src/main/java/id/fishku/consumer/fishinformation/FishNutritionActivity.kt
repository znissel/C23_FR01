package id.fishku.consumer.fishinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.fishku.consumer.databinding.ActivityFishNutritionBinding

class FishNutritionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishNutritionBinding
    private lateinit var nutritionViewModel: FishNutritionViewModel
    private lateinit var nutritionAdapter: FishNutritionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nutritionViewModel = ViewModelProvider(this).get(FishNutritionViewModel::class.java)

        setUpAction()
        setupRecyclerView()
        observeFishNutrition()

    }

    private fun setUpAction() {
        binding.toolbarRecipe.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        nutritionAdapter = FishNutritionAdapter { nutritionId ->
            val intent = Intent(this@FishNutritionActivity, FishInformationActivity::class.java)
            startActivity(intent)
        }

        binding.rvFishNutrition.apply {
            layoutManager = LinearLayoutManager(this@FishNutritionActivity)
            adapter = nutritionAdapter
        }
    }

    private fun observeFishNutrition() {
        lifecycleScope.launchWhenStarted {
            nutritionViewModel.fishNutrition.collect { fishNutrition ->
                nutritionAdapter.setFishNutrition(fishNutrition)
            }
        }
    }
}