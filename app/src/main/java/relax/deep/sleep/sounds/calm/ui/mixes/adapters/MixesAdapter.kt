package relax.deep.sleep.sounds.calm.ui.mixes.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import relax.deep.sleep.sounds.calm.databinding.ItemMixBinding
import relax.deep.sleep.sounds.calm.model.Mix
import relax.deep.sleep.sounds.calm.ui.mixes.MixesEvent
import relax.deep.sleep.sounds.calm.utils.PremiumPreferences

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
        binding.crossIv.setOnClickListener { event.value = MixesEvent.OnDeleteMixClick(mix) }
        binding.mixItemTv.text = mix.title
        if (mix.isPremium && !PremiumPreferences.hasPremiumStatus(binding.root.context)) {
            binding.crownIv.visibility = View.VISIBLE
            binding.crossIv.visibility = View.GONE
        } else {
            if (mix.isCustom) {
                binding.crossIv.visibility = View.VISIBLE
            } else {
                binding.crossIv.visibility = View.GONE
            }
            binding.crownIv.visibility = View.GONE
        }
        binding.mixItemIv.setImageURI(mix.picturePath)
    }
}


private class MixDiffCallback : DiffUtil.ItemCallback<Mix>() {
    override fun areItemsTheSame(oldItem: Mix, newItem: Mix): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(oldItem: Mix, newItem: Mix): Boolean =
        (oldItem == newItem)
}
