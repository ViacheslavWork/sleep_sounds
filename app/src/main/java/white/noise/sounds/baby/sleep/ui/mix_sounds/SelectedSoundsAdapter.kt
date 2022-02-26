package white.noise.sounds.baby.sleep.ui.mix_sounds

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.request.RequestOptions
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ItemSoundsBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent

class SelectedSoundsAdapter(
    val event: MutableLiveData<SoundsEvent>,
    private val isClosable: Boolean = false,
    private val isSoundChangeable: Boolean = false,
    private val background: Drawable? = null
) :
    ListAdapter<Sound, SelectedSoundsHolder>(SoundDiffCallback()) {
    private val removedItems = mutableListOf<Sound>()

    lateinit var binding: ItemSoundsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedSoundsHolder {
        binding =
            ItemSoundsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedSoundsHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedSoundsHolder, position: Int) {
        getItem(position).let { sound -> holder.onBind(sound, isClosable, isSoundChangeable) }

        background?.let { binding.root.background = background }

        binding.removeIv.setOnClickListener {
//            TODO()
            /* currentList.removeAt(position)
             notifyItemRemoved(position)
             notifyItemRangeChanged(position, currentList.size);
             val size = currentList.size*/
            val pos = position
            removeItem(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(0, currentList.size)
            event.value =
                SoundsEvent.AdditionalSoundsEvent.OnRemoveClick(getItem(position), position)
//            removeItem(position)
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                event.value = SoundsEvent.AdditionalSoundsEvent.OnSeekBarChanged(progress = p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.root.setOnClickListener {
            event.value = SoundsEvent.OnSoundClick(getItem(position))
        }

    }

    fun removeItem(position: Int): Sound? {
        if (position >= itemCount) return null
        val item = currentList[position]
        removedItems.add(item)
        val actualList = currentList - removedItems
        if (actualList.isEmpty()) removedItems.clear()
        this.submit(actualList, true)
        return item
    }

    private fun submit(list: List<Sound>?, isLocalSubmit: Boolean) {
        if (!isLocalSubmit) removedItems.clear()
        super.submitList(list)
    }
}

class SelectedSoundsHolder(private val binding: ItemSoundsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_sound_placeholder)
            .fallback(R.drawable.ic_sound_placeholder)
    }

    fun onBind(sound: Sound, isClosable: Boolean, isSoundChangeable: Boolean) {
        binding.mixItemTv.text = sound.title
        if (isClosable) binding.removeIv.visibility = View.VISIBLE
        if (isSoundChangeable) binding.seekBar.visibility = View.VISIBLE

        val imageLoader = ImageLoader.Builder(binding.root.context)
            .componentRegistry { add(SvgDecoder(binding.root.context)) }
            .build()

        val request = ImageRequest.Builder(binding.root.context)
            .placeholder(R.drawable.ic_sound_placeholder)
            .error(R.drawable.ic_sound_placeholder)
            .data(Uri.parse("file:///android_asset/icons/${sound.icon}"))
            .target(binding.soundsItemIv)
            .build()

        imageLoader.enqueue(request)
    }
}

private class SoundDiffCallback : DiffUtil.ItemCallback<Sound>() {
    override fun areItemsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        (oldItem == newItem)
}