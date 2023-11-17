package id.fishku.consumer.faq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.fishku.consumer.R
import id.fishku.consumer.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding

    //TAMBAHAN
    //private lateinit var adapter: FaqAdapter
    private val faqList = ArrayList<Questions>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
        hideActionBar()
        setupFaq()
    }

    private fun setUpAction() {
        binding.toolbarFaq.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun setupFaq() {
        binding.rvFaq.setHasFixedSize(true)

        faqList.addAll(getListFaq())
        binding.rvFaq.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = FaqAdapter(faqList)
        binding.rvFaq.adapter = listHeroAdapter
    }

    private fun getListFaq(): ArrayList<Questions> {
        val questions = resources.getStringArray(R.array.data_questions)
        val answer = resources.getStringArray(R.array.data_answer)
        for (i in questions.indices) {
            val q = Questions(questions[i], answer[i])
            faqList.add(q)
        }
        return faqList
    }
}