package white.noise.sounds.baby.sleep.ui.sounds

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ItemSoundsBinding
import white.noise.sounds.baby.sleep.databinding.SectionRowBinding
import white.noise.sounds.baby.sleep.model.Sound

private const val TAG = "SectionAdapter"

class SectionAdapter(val event: MutableLiveData<SoundsEvent> = MutableLiveData()) :
    ListAdapter<Section, SectionHolder>(SectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder {
        val binding: SectionRowBinding =
            SectionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionHolder, position: Int) {
        getItem(position).let { section -> holder.onBind(section = section) }
        /*holder.itemView.setOnClickListener {
            event.value = SoundsEvent.OnSoundClick(getItem(position))
        }*/
    }
}

class SoundAdapter(val event: MutableLiveData<SoundsEvent> = MutableLiveData()) :
    ListAdapter<Sound, SoundsHolder>(SoundDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsHolder {
        val binding: ItemSoundsBinding =
            ItemSoundsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundsHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundsHolder, position: Int) {
        getItem(position).let { sound -> holder.onBind(sound) }
        holder.itemView.setOnClickListener {
            event.value = SoundsEvent.OnSoundClick(getItem(position))
        }
    }
}


class SectionHolder(private val binding: SectionRowBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var adapter: SoundAdapter

    fun onBind(section: Section) {
        binding.sectionNameTv.text = section.sectionName.toString()
        binding.sectionRv.layoutManager = GridLayoutManager(binding.root.context, 3)
        adapter = SoundAdapter()
        binding.sectionRv.adapter = adapter
        adapter.submitList(section.items)
    }
}

class SoundsHolder(private val binding: ItemSoundsBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_sound_placeholder)
            .fallback(R.drawable.ic_sound_placeholder)
    }

    fun onBind(sound: Sound) {
        binding.mixItemTv.text = sound.title
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
}

private class SoundDiffCallback : DiffUtil.ItemCallback<Sound>() {
    override fun areItemsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        (oldItem == newItem)
}

private class SectionDiffCallback : DiffUtil.ItemCallback<Section>() {
    override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean =
        (oldItem.sectionName == newItem.sectionName)

    override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean =
        (oldItem == newItem)
}




