package id.fishku.consumer.fishrecipe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.fishku.consumer.databinding.ActivityFishRecipeBinding

class FishRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishRecipeBinding
    private lateinit var recipeViewModel: FishRecipeViewModel
    private lateinit var recipeAdapter: FishRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipeViewModel = ViewModelProvider(this).get(FishRecipeViewModel::class.java)

        hideActionBar()
        setUpAction()
        setupRecyclerView()
        observeFishRecipes()
    }

    private fun setUpAction() {
        binding.toolbarRecipe.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    //setup RV
    private fun setupRecyclerView() {
        recipeAdapter = FishRecipeAdapter { recipeId ->
            val selectedRecipe = recipeAdapter.getRecipeById(recipeId)
            selectedRecipe?.let {
                openYouTubeWithRecipeName(it.recipeName)
            }
        }

        binding.rvFishRecipe.apply {
            layoutManager = LinearLayoutManager(this@FishRecipeActivity)
            adapter = recipeAdapter
        }
    }

    private fun observeFishRecipes() {
        lifecycleScope.launchWhenStarted {
            recipeViewModel.fishRecipes.collect { fishRecipes ->
                recipeAdapter.setRecipes(fishRecipes)
            }
        }
    }

    //intent youtube
    private fun openYouTubeWithRecipeName(recipeName: String) {
        val youtubeIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.youtube.com/results?search_query=$recipeName")
        )
        startActivity(youtubeIntent)
    }
}