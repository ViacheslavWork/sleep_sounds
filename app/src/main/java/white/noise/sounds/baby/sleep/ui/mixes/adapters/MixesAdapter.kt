package white.noise.sounds.baby.sleep.ui.mixes.adapters


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ItemMixBinding
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.ui.mixes.MixesEvent

private const val TAG = "MixAdapter"

class MixesAdapter(val event: MutableLiveData<MixesEvent.OnMixClick> = MutableLiveData()) :
    ListAdapter<Mix, MixesViewHolder>(MixDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MixesViewHolder {
        val binding: ItemMixBinding =
            ItemMixBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MixesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MixesViewHolder, position: Int) {
        getItem(position).let { mix -> holder.onBind(mix) }
        holder.itemView.setOnClickListener {
            event.value = MixesEvent.OnMixClick(getItem(position))
        }
    }
}

class MixesViewHolder(private val binding: ItemMixBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_autumn)
            .fallback(R.drawable.ic_autumn)
    }

    fun onBind(mix: Mix) {
        binding.mixItemTv.text = mix.title
        Glide.with(itemView.context)
            .load(
                Uri.parse(
                    "file:///android_asset/mixes/${
                        mix.category.toString().lowercase()
                    }/${mix.picturePath}"
                )
            )
            .error(R.drawable.ic_autumn)
            .apply(imageOption)
            .into(binding.mixItemIv)
    }
}


private class MixDiffCallback : DiffUtil.ItemCallback<Mix>() {
    override fun areItemsTheSame(oldItem: Mix, newItem: Mix): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(oldItem: Mix, newItem: Mix): Boolean =
        (oldItem == newItem)
}
