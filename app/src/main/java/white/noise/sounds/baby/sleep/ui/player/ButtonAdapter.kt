package white.noise.sounds.baby.sleep.ui.player

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.request.RequestOptions
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ButtonPlayerBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent

class ButtonAdapter(
    val event: MutableLiveData<SoundsEvent>,
    private val isClosable: Boolean = false,
    private val isSoundChangeable: Boolean = false,
    private val background: Drawable? = null
) :
    ListAdapter<Sound, SoundsHolder>(SoundDiffCallback()) {
    lateinit var binding: ButtonPlayerBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsHolder {
        binding =
            ButtonPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundsHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundsHolder, position: Int) {
        getItem(position).let { sound -> holder.onBind(sound, isClosable, isSoundChangeable) }

        background?.let { binding.root.background = background }

        /*binding.removeIv.setOnClickListener {
            event.value = SoundsEvent.AdditionalSoundsEvent.OnRemoveClick(getItem(position))
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                event.value = SoundsEvent.AdditionalSoundsEvent.OnSeekBarChanged(progress = p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })*/
        holder.itemView.setOnClickListener {
            event.value = SoundsEvent.OnSoundClick(getItem(position))
        }
    }
}

class SoundsHolder(private val binding: ButtonPlayerBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_sound_placeholder)
            .fallback(R.drawable.ic_sound_placeholder)
    }

    fun onBind(sound: Sound, isClosable: Boolean, isSoundChangeable: Boolean) {
        binding.btnTv.text = sound.title
//        if (isClosable) binding.removeIv.visibility = View.VISIBLE
//        if (isSoundChangeable) binding.seekBar.visibility = View.VISIBLE
        /*Glide.with(binding.root.context)
            .load(Uri.parse("file:///android_asset/icons/${sound.icon}"))
//            .load(R.drawable.ic_sound_placeholder)
            .error(R.drawable.ic_sound_placeholder)
            .apply(imageOption)
            .into(binding.soundsItemIv)*/
        /* val id = binding.root.resources.getIdentifier(
             "laugh5",
             "drawable",
             binding.root.context.packageName
         )
         Log.i(TAG, "onBind: $id")


         binding.soundsItemIv.setImageDrawable(
             ResourcesCompat.getDrawable(
                 binding.root.context.resources,
                 id, null
             )
         )*/

        val imageLoader = ImageLoader.Builder(binding.root.context)
            .componentRegistry { add(SvgDecoder(binding.root.context)) }
            .build()

        val request = ImageRequest.Builder(binding.root.context)
//            .crossfade(true)
//            .crossfade(500)
            .placeholder(R.drawable.ic_sound_placeholder)
            .error(R.drawable.ic_sound_placeholder)
            .data(Uri.parse("file:///android_asset/icons/${sound.icon}"))
            .target(binding.playerBtn)
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