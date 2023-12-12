package id.fishku.consumer.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.fishku.consumer.R
import id.fishku.consumer.databinding.ViewFilterButtonBinding

class FishFilterAdapter(private val onItemClick: (Filter) -> Unit) :
    ListAdapter<UiFilter, FishFilterAdapter.ListViewHolder>(DIFF_CALLBACK) {

    inner class ListViewHolder(private val binding: ViewFilterButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uiFilter: UiFilter) {
            binding.apply {
                tvFilterName.text = uiFilter.filter.displayText
                root.setOnClickListener {
                    onItemClick(uiFilter.filter)
                }
                val cardBackgroundColorResId = if (uiFilter.isSelected) {
                    R.color.blue
                } else {
                    R.color.blue_100
                }
                root.setCardBackgroundColor(ContextCompat.getColor(root.context, cardBackgroundColorResId))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ViewFilterButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val uiFilter = getItem(position)
        holder.bind(uiFilter)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiFilter>() {
            override fun areItemsTheSame(oldItem: UiFilter, newItem: UiFilter): Boolean =
                oldItem.filter.filterID == newItem.filter.filterID

            override fun areContentsTheSame(oldItem: UiFilter, newItem: UiFilter): Boolean =
                oldItem == newItem
        }
    }
}