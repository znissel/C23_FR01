package id.fishku.consumer.fishinformation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
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
        getIntentDetail()
    }

    private fun setUpAction() {
        binding.toolbarInformation.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun getIntentDetail() {
        id = intent.getStringExtra(ID) ?: ""
        name = intent.getStringExtra(NAME) ?: ""
        picture = intent.getStringExtra(PICTURE) ?: ""
        protein = intent.getStringExtra(PROTEIN) ?: ""

        kalium = intent.getStringExtra(KALIUM) ?: ""
        vitb12 = intent.getStringExtra(VITB12) ?: ""
        vitb6 = intent.getStringExtra(VITB6) ?: ""
        zatbesi = intent.getStringExtra(ZATBESI) ?: ""
        magnesium = intent.getStringExtra(MAGNESIUM) ?: ""
        fosfor = intent.getStringExtra(FOSFOR) ?: ""
        karbohidrat = intent.getStringExtra(KARBOHIDRAT) ?: ""
        lemak = intent.getStringExtra(LEMAK) ?: ""
        natrium = intent.getStringExtra(NATRIUM) ?: ""
        kalsium = intent.getStringExtra(KALSIUM) ?: ""


        binding.tvSelectedFish.text = name
        Glide.with(this)
            .load(picture)
            .into(binding.ivFish)
        binding.tvProteinValue.text = protein
        binding.tvKaliumValue.text = kalium
        binding.tvVitB12value.text = vitb12
        binding.tvVitB6value.text = vitb6
        binding.tvZatBesiValue.text = zatbesi
        binding.tvMagnesiumValue.text = magnesium
        binding.tvFosforValue.text = fosfor
        binding.tvKarbohidratValue.text = karbohidrat
        binding.tvLemakValue.text = lemak
        binding.tvNatriumValue.text = natrium
        binding.tvKalsiumValue.text = kalsium
    }

    companion object {
        const val ID = "ID"
        const val NAME = "NAME"
        const val PICTURE = "PICTURE"
        const val PROTEIN = "PROTEIN"
        const val KALIUM = "KALIUM"
        const val VITB12 = "VITB12"
        const val VITB6 = "VITB6"
        const val ZATBESI = "ZATBESI"
        const val MAGNESIUM = "MAGNESIUM"
        const val FOSFOR = "FOSFOR"
        const val KARBOHIDRAT = "KARBOHIDRAT"
        const val LEMAK = "LEMAK"
        const val NATRIUM = "NATRIUM"
        const val KALSIUM = "KALSIUM"

        var id: String = ""
        var name: String = ""
        var picture: String? = null
        var protein: String = ""
        var kalium: String = ""
        var vitb12: String = ""
        var vitb6: String = ""
        var zatbesi: String = ""
        var magnesium: String = ""
        var fosfor: String = ""
        var karbohidrat: String = ""
        var lemak: String = ""
        var natrium: String = ""
        var kalsium: String = ""

    }
}