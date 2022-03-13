package white.noise.sounds.baby.sleep.ui.sounds.custom_mix_dialog

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ItemCustomDialogBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent

class CustomMixAdapter(val event: MutableLiveData<SoundsEvent> = MutableLiveData()) :
    ListAdapter<Sound, SoundHolder>(SoundDiffCallback()) {
    lateinit var binding: ItemCustomDialogBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
        binding =
            ItemCustomDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundHolder(binding)
    }


    override fun onBindViewHolder(holder: SoundHolder, position: Int) {
        holder.onBind(getItem(position),event)
    }
}

class SoundHolder(private val binding: ItemCustomDialogBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(sound: Sound, event: MutableLiveData<SoundsEvent>) {
        binding.volumeSeekBar.progress = sound.volume
        binding.volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                sound.volume = p1
                event.value = SoundsEvent.OnSeekBarChanged(sound)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        setIcon(sound)

        binding.cross.setOnClickListener {
            sound.volume = 0
            binding.volumeSeekBar.progress = 0
        }
    }

    private fun setIcon(sound: Sound) {
        binding.icon.setImageResource(sound.icon)

        /*val imageLoader = ImageLoader.Builder(binding.root.context)
            .componentRegistry { add(SvgDecoder(binding.root.context)) }
            .build()

        val request = ImageRequest.Builder(binding.root.context)
            .placeholder(R.drawable.ic_sound_placeholder)
            .error(R.drawable.ic_sound_placeholder)
            .data(Uri.parse("file:///android_asset/icons/${sound.icon}"))
            .target(binding.icon)
            .build()

        imageLoader.enqueue(request)*/
    }
}

class SoundDiffCallback : DiffUtil.ItemCallback<Sound>() {

    override fun areItemsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        oldItem == newItem
}