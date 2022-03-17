package white.noise.sounds.baby.sleep.ui.mixes.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import white.noise.sounds.baby.sleep.databinding.ItemMixBinding
import white.noise.sounds.baby.sleep.model.Mix
import white.noise.sounds.baby.sleep.ui.mixes.MixesEvent

private const val TAG = "MixAdapter"

class MixesAdapter(val event: MutableLiveData<MixesEvent> = MutableLiveData()) :
    ListAdapter<Mix, MixesViewHolder>(MixDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MixesViewHolder {
        val binding: ItemMixBinding =
            ItemMixBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MixesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MixesViewHolder, position: Int) {
        getItem(position).let { mix -> holder.onBind(mix, event) }
    }
}

class MixesViewHolder(private val binding: ItemMixBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(mix: Mix, event: MutableLiveData<MixesEvent>) {
        binding.root.setOnClickListener { event.value = MixesEvent.OnMixClick(mix) }
        binding.mixItemTv.text = mix.title
        if (mix.isPremium) binding.crownIv.visibility = View.VISIBLE
        else binding.crownIv.visibility = View.GONE
        binding.mixItemIv.setImageURI(mix.picturePath)
        /*      Glide.with(itemView.context)
                  .load(mix.picturePath)
                  .error(R.drawable.ic_autumn)
                  .apply(imageOption)
                  .into(binding.mixItemIv)*/
    }
}


private class MixDiffCallback : DiffUtil.ItemCallback<Mix>() {
    override fun areItemsTheSame(oldItem: Mix, newItem: Mix): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(oldItem: Mix, newItem: Mix): Boolean =
        (oldItem == newItem)
}
