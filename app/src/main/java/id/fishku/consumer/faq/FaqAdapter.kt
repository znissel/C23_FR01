package id.fishku.consumer.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.fishku.consumer.databinding.ViewFaqBinding

//nanti perlu dipindah ke core mgkn kayak yg lain
class FaqAdapter(private val faqList: ArrayList<Questions>) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>(){ //ListAdapter<Questions, FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = ViewFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val question = faqList[position]
        holder.bind(question)

        val isExpandable = faqList[position].expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.linearLayout.setOnClickListener {
            val questions = faqList[position]
            questions.expandable = !questions.expandable
            notifyItemChanged(position)
        }
    }

    class FaqViewHolder(val binding: ViewFaqBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(q: Questions) {
            with(binding) {
                faqQuestion.text = q.question
                faqAnswer.text= q.answer
            }
        }

        var expandableLayout = binding.expandableLayout
        var linearLayout = binding.linearLayout
    }

    /*companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Questions>() {
            override fun areItemsTheSame(oldItem: Questions, newItem: Questions): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Questions,
                newItem: Questions,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }*/
}