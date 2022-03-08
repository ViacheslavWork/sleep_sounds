package white.noise.sounds.baby.sleep.ui.sounds

import android.net.Uri
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
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ItemSoundsBinding
import white.noise.sounds.baby.sleep.databinding.SectionRowBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.service.PlayerService


private const val TAG = "SectionAdapter"

class SectionAdapter(
    val event: MutableLiveData<SoundsEvent> = MutableLiveData(),
    private val isSelectable: Boolean = true
) :
    ListAdapter<Section, SectionHolder>(SectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder {
        val binding: SectionRowBinding =
            SectionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionHolder(binding, isSelectable)
    }

    override fun onBindViewHolder(holder: SectionHolder, position: Int) {
        getItem(position).let { section -> holder.onBind(section = section, event = event) }
    }
}

class SectionHolder(private val binding: SectionRowBinding, private val isSelectable: Boolean) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var adapter: SoundAdapter
    fun onBind(section: Section, event: MutableLiveData<SoundsEvent>) {
        binding.sectionNameTv.text = section.soundCategory.title
        binding.sectionRv.layoutManager = GridLayoutManager(binding.root.context, 3)
        adapter = SoundAdapter(event, isSelectable = isSelectable)
        binding.sectionRv.adapter = adapter
        adapter.submitList(section.items)
    }

}

class SoundAdapter(
    val event: MutableLiveData<SoundsEvent>,
    private val isSelectable: Boolean
) :
    ListAdapter<Sound, SoundsHolder>(SoundDiffCallback()) {

    lateinit var binding: ItemSoundsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsHolder {
        binding =
            ItemSoundsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundsHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundsHolder, position: Int) {
//        getItem(position).let { sound -> holder.onBind(sound) }
        holder.onBind(getItem(position), event, isSelectable)
//        background?.let { binding.root.background = background }

    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}

class SoundsHolder(private val binding: ItemSoundsBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_sound_placeholder)
            .fallback(R.drawable.ic_sound_placeholder)
    }

    fun onBind(sound: Sound, event: MutableLiveData<SoundsEvent>, isSelectable: Boolean) {
        binding.mixItemTv.text = sound.title
        binding.seekBar.visibility = View.INVISIBLE
        if (isSelectable) {
            if (sound.isPlaying) {
                binding.seekBar.visibility = View.VISIBLE
                binding.root.background = ResourcesCompat.getDrawable(
                    binding.root.resources,
                    R.drawable.gradient_liner_bg5_rounded_corners,
                    null
                )
//                initExoPlayer()
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
            sound.isPlaying = !sound.isPlaying
            event.value = SoundsEvent.OnSoundClick(sound)
            bindingAdapter?.notifyItemChanged(bindingAdapterPosition)
        }

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
            .target(binding.soundsItemIv)
            .build()

        imageLoader.enqueue(request)
    }

    fun initExoPlayer() {
        val exoPlayer = ExoPlayer.Builder(binding.root.context).build()
        val mediaItem1 =
            MediaItem.fromUri(Uri.parse("file:///android_asset/sounds/animal/frog.ogg"))
        val mediaItem2 =
            MediaItem.fromUri(Uri.parse("file:///android_asset/sounds/animal/wolf.ogg"))
        val defaultDataSourceFactory = DefaultDataSourceFactory(
            binding.root.context,
            "audio / ogg"
        ) // userAgent -> audio / mpeg не может быть пустым
        val mediaSourceFactory = DefaultMediaSourceFactory(defaultDataSourceFactory)

        val mediaSource1 = mediaSourceFactory.createMediaSource(mediaItem1)
        val mediaSource2 = mediaSourceFactory.createMediaSource(mediaItem2)

        val mergingMediaSource = MergingMediaSource(mediaSource2, mediaSource1)
        val loopMediaSource1 = LoopingMediaSource(mediaSource1)
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        exoPlayer.setMediaSource(mediaSource1)
        exoPlayer.playWhenReady = true
        exoPlayer.prepare()


        val loopMediaSource2 = LoopingMediaSource(mediaSource2)
        val exoPlayer2 = ExoPlayer.Builder(binding.root.context).build()
        exoPlayer2.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        exoPlayer2.setMediaSource(mediaSource2)
        exoPlayer2.playWhenReady = true
        exoPlayer2.prepare()
    }

}

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




