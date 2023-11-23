package id.fishku.consumer.detailshop

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.fishku.consumer.R
import id.fishku.consumer.databinding.ActivityDetailShopBinding

class DetailShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Contoh implicit intent untuk kunjungi toko
        binding.btnVisitStore.setOnClickListener {
            openWebsite("https://maps.app.goo.gl/7QHCidEfW3Lzr4ie9")
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {

        }
    }
}