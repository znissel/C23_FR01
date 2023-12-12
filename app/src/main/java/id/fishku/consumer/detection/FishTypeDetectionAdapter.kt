package id.fishku.consumer.detection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.fishku.consumer.core.databinding.ItemFishTypeBinding
import id.fishku.consumer.model.FishTypeDetection

class FishTypeDetectionAdapter(private val onItemClick: (FishTypeDetection) -> Unit) :
    ListAdapter<FishTypeDetection, FishTypeDetectionAdapter.ListViewHolder>(DIFF_CALLBACK) {

    inner class ListViewHolder(private val binding: ItemFishTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fish: FishTypeDetection) {
            binding.apply {
                itemTvName.text = fish.fishName
                Glide.with(root).load(fish.photoUrl).into(itemImgPhoto)
                root.setOnClickListener {
                    onItemClick(fish)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemFishTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val fish = getItem(position)
        holder.bind(fish)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FishTypeDetection>() {
            override fun areItemsTheSame(
                oldItem: FishTypeDetection,
                newItem: FishTypeDetection
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: FishTypeDetection,
                newItem: FishTypeDetection
            ): Boolean =
                oldItem.id == newItem.id
        }
    }
}

