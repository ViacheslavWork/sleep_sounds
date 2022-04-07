package calm.sleep.relaxing.sounds.noise.ui.go_premium

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import calm.sleep.relaxing.sounds.noise.databinding.ItemGoPremiumBinding

private const val TAG = "TickerAdapter"

class TickerAdapter : ListAdapter<Picture, TickerHolder>(FeatureDiffCallback()) {

    lateinit var binding: ItemGoPremiumBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerHolder {
        binding =
            ItemGoPremiumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TickerHolder(binding)
    }


    override fun onBindViewHolder(holder: TickerHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}

class TickerHolder(private val binding: ItemGoPremiumBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(feature: Picture) {
        binding.imageView.setImageResource(feature.iconResource)
    }
}

class FeatureDiffCallback : DiffUtil.ItemCallback<Picture>() {

    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean =
        oldItem.iconResource == newItem.iconResource

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean =
        oldItem == newItem
}

data class Picture(
    @DrawableRes val iconResource: Int,
    val contentDescription: String,
)
