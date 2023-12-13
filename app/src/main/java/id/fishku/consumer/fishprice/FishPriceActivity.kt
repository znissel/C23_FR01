package id.fishku.consumer.fishprice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.fishku.consumer.R
import id.fishku.consumer.databinding.ActivityFishPriceBinding

class FishPriceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFishPriceBinding
    private lateinit var priceViewModel: FishPriceViewModel
    private lateinit var priceAdapter: FishPriceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishPriceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        priceViewModel = ViewModelProvider(this).get(FishPriceViewModel::class.java)

        priceAdapter = FishPriceAdapter(priceViewModel.getFishPrices())
        binding.rvFishPrice.adapter = priceAdapter
        binding.rvFishPrice.layoutManager = LinearLayoutManager(this)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_fish_price)
        recyclerView.setHasFixedSize(true)

        setUpAction()
        hideActionBar()


    }

    private fun setUpAction() {
        binding.toolbarRecipe.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}