package relax.deep.sleep.sounds.calm.ui.sounds

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import relax.deep.sleep.sounds.calm.BuildConfig
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.ItemSoundsBinding
import relax.deep.sleep.sounds.calm.databinding.SectionRowBinding
import relax.deep.sleep.sounds.calm.model.Sound


private const val TAG = "SectionAdapter"

class SectionAdapter(
    val event: MutableLiveData<SoundsEvent> = MutableLiveData(),
    private val isSelectable: Boolean = true,
    private val isSoundChangeable: Boolean = true
) :
    ListAdapter<Section, SectionHolder>(SectionDiffCallback()) {
    private val mapSoundIdToSoundHolderData: MutableMap<Long, SoundHolderData> = mutableMapOf()

    fun getMapSoundIdToHolder(): Map<Long, SoundHolderData> {
        return mapSoundIdToSoundHolderData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder {
        val binding: SectionRowBinding =
            SectionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionHolder(binding, isSelectable, isSoundChangeable, mapSoundIdToSoundHolderData)
    }

    override fun onBindViewHolder(holder: SectionHolder, position: Int) {
        getItem(position).let { section -> holder.onBind(section = section, event = event) }
    }
}

class SectionHolder(
    private val binding: SectionRowBinding,
    private val isSelectable: Boolean,
    private val isSoundChangeable: Boolean,
    private val mapSoundIdToSoundHolderData: MutableMap<Long, SoundHolderData>
) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var adapter: SoundAdapter
    fun onBind(section: Section, event: MutableLiveData<SoundsEvent>) {
        binding.sectionNameTv.text = section.soundCategory.title
        binding.sectionRv.layoutManager = GridLayoutManager(binding.root.context, 3)
        adapter =
            SoundAdapter(
                event,
                isSelectable = isSelectable,
                isSoundChangeable = isSoundChangeable,
                mapSoundIdToSoundHolderData = mapSoundIdToSoundHolderData
            )
        binding.sectionRv.adapter = adapter
        adapter.submitList(section.items)
    }
}

class SoundAdapter(
    val event: MutableLiveData<SoundsEvent>,
    private val isSelectable: Boolean,
    private val isSoundChangeable: Boolean,
    private val mapSoundIdToSoundHolderData: MutableMap<Long, SoundHolderData>
) :
    ListAdapter<Sound, SoundsHolder>(SoundDiffCallback()) {

    lateinit var binding: ItemSoundsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsHolder {
        binding =
            ItemSoundsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundsHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundsHolder, position: Int) {
        mapSoundIdToSoundHolderData[getItem(position).id] =
            SoundHolderData(this, position)
        holder.onBind(
            getItem(position),
            event,
            isSelectable,
            isSoundChangeable
        )
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}

class SoundsHolder(private val binding: ItemSoundsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        sound: Sound,
        event: MutableLiveData<SoundsEvent>,
        isSelectable: Boolean,
        isSoundChangeable: Boolean
    ) {
        binding.mixItemTv.text = sound.title
        binding.seekBar.visibility = View.INVISIBLE
        if (sound.isPremium) binding.crownIv.visibility = View.VISIBLE
        if (isSelectable) {
            if (sound.isPlaying) {
                binding.seekBar.visibility = View.VISIBLE
                binding.root.background = ResourcesCompat.getDrawable(
                    binding.root.resources,
                    R.drawable.gradient_liner_bg5_rounded_corners,
                    null
                )
                Log.i(TAG, "onBind: $sound")
            } else {
                binding.seekBar.visibility = View.INVISIBLE
                binding.root.background = ResourcesCompat.getDrawable(
                    binding.root.resources,
                    R.drawable.gradient_liner_bg4_rounded_corners,
                    null
                )
            }
        }
        if (!isSoundChangeable) binding.seekBar.visibility = View.GONE
        binding.seekBar.progress = sound.volume
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                sound.volume = p1
                event.value = SoundsEvent.OnSeekBarChanged(sound)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.root.setOnClickListener {
            if (!sound.isPremium) {
//                sound.isPlaying = !sound.isPlaying
//                bindingAdapter?.notifyItemChanged(bindingAdapterPosition,sound)
                event.value = SoundsEvent.OnSoundClick(sound, this)
            } else {
                event.value = SoundsEvent.OnSoundClick(sound, this)
            }
        }
        binding.soundsItemIv.setImageResource(sound.icon)
    }

}

data class SoundHolderData(val adapter: SoundAdapter, val position: Int)

private class SoundDiffCallback : DiffUtil.ItemCallback<Sound>() {
    override fun areItemsTheSame(oldItem: Sound, newItem: Sound): Boolean {
        return (oldItem.id == newItem.id)
    }

    override fun areContentsTheSame(oldItem: Sound, newItem: Sound): Boolean {
        return (oldItem == newItem)
    }
}

private class SectionDiffCallback : DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
        return (oldItem.soundCategory == newItem.soundCategory)
    }

    override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
        return (oldItem == newItem)
    }
}




