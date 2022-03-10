package white.noise.sounds.baby.sleep.ui.mix_sounds

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
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
    val event: MutableLiveData<SoundsEvent> = MutableLiveData(),
) :
    ListAdapter<Sound, SelectedSoundsHolder>(SoundDiffCallback()) {

    lateinit var binding: ItemSoundsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedSoundsHolder {
        binding =
            ItemSoundsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedSoundsHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedSoundsHolder, position: Int) {
        getItem(position).let { sound -> holder.onBind(sound, event) }
    }
}

class SelectedSoundsHolder(private val binding: ItemSoundsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_sound_placeholder)
            .fallback(R.drawable.ic_sound_placeholder)
    }

    fun onBind(sound: Sound, event: MutableLiveData<SoundsEvent>) {
        binding.mixItemTv.text = sound.title
        binding.removeIv.visibility = View.VISIBLE
        binding.seekBar.visibility = View.VISIBLE

        binding.seekBar.progress = sound.volume
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                event.value = SoundsEvent.OnSeekBarChanged(sound.apply { volume = p1 })
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        binding.root.setOnClickListener {
            event.value = SoundsEvent.OnSoundClick(sound)
        }
        binding.removeIv.setOnClickListener {
            event.value =
                SoundsEvent.AdditionalSoundsEvent.OnRemoveClick(sound, bindingAdapterPosition)
        }

        binding.root.background = ResourcesCompat.getDrawable(
            binding.root.resources,
            R.drawable.gradient_liner_bg3_rounded_corners,
            null
        )

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