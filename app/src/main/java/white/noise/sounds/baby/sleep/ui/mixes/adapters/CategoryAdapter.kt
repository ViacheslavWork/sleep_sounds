package white.noise.sounds.baby.sleep.ui.mixes.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ItemMixCategoryBinding
import white.noise.sounds.baby.sleep.ui.mixes.MixesEvent
import white.noise.sounds.baby.sleep.ui.mixes.SelectableMixCategory

private const val TAG = "CategoryAdapter"

class CategoryAdapter(val event: MutableLiveData<MixesEvent.OnCategoryClick> = MutableLiveData()) :
    ListAdapter<SelectableMixCategory, MixesCategoryViewHolder>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MixesCategoryViewHolder {
        val binding: ItemMixCategoryBinding =
            ItemMixCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MixesCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MixesCategoryViewHolder, position: Int) {
        getItem(position).let { mixCategory -> holder.onBind(mixCategory) }
        holder.itemView.setOnClickListener {
            event.value = MixesEvent.OnCategoryClick(getItem(position), position = position)
        }
    }
}

class MixesCategoryViewHolder(private val binding: ItemMixCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.mix_placeholder)
            .fallback(R.drawable.mix_placeholder)
    }

    fun onBind(mixCategory: SelectableMixCategory) {
        binding.categoryBtn.text = mixCategory.category.toString()
        if (mixCategory.isSelected) {
            binding.categoryBtn.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.blue40
                )
            )
        } else {
            binding.categoryBtn.setBackgroundColor(80000000)
        }
    }
}


private class CategoryDiffCallback : DiffUtil.ItemCallback<SelectableMixCategory>() {
    override fun areItemsTheSame(
        oldItem: SelectableMixCategory, newItem: SelectableMixCategory
    ): Boolean = (oldItem.category == newItem.category)

    override fun areContentsTheSame(oldItem: SelectableMixCategory, newItem: SelectableMixCategory):
            Boolean = (oldItem.isSelected == newItem.isSelected)
}
