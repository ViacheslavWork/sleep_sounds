package calm.sleep.relaxing.sounds.noise.ui.mix_sounds

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import calm.sleep.relaxing.sounds.noise.R
import calm.sleep.relaxing.sounds.noise.databinding.ItemSoundsBinding
import calm.sleep.relaxing.sounds.noise.databinding.SectionRowBinding
import calm.sleep.relaxing.sounds.noise.model.Sound
import calm.sleep.relaxing.sounds.noise.model.SoundCategory
import calm.sleep.relaxing.sounds.noise.ui.sounds.SoundsEvent
import calm.sleep.relaxing.sounds.noise.utils.MyLog.showLog
import calm.sleep.relaxing.sounds.noise.utils.PremiumPreferences

private const val TAG = "SectionAdapter"
private val mapSoundIdToPositionInInternalAdapter = mutableMapOf<Long, Int>()
private val mapSoundCategoryToInternalAdapter = mutableMapOf<SoundCategory, SoundAdapter>()
private var mapSoundCategoryToSounds: MutableMap<SoundCategory, List<Sound>> = mutableMapOf()

class GroupSoundsAdapter(
    var sounds: List<Sound>,
    val event: MutableLiveData<SoundsEvent> = MutableLiveData(),
    private val isSelectable: Boolean = true,
    private val isSoundChangeable: Boolean = false
) :
    RecyclerView.Adapter<GroupSoundsHolder>() {
    private var categories: List<SoundCategory>

    init {
        mapSoundCategoryToSounds.putAll(sounds.groupBy { it.category })
        categories = mapSoundCategoryToSounds.keys.toList()
    }

    fun updateSounds(sounds: List<Sound>) {
        this.sounds = sounds
        mapSoundCategoryToSounds.clear()
        mapSoundCategoryToSounds.putAll(this.sounds.groupBy { it.category })
        mapSoundCategoryToInternalAdapter.forEach { (category, adapter) ->
            adapter.updateItems(mapSoundCategoryToSounds[category]!!)
        }
    }

    fun notifySoundChanged(sound: Sound) {
        val adapter = mapSoundCategoryToInternalAdapter[sound.category]
        adapter?.notifyItemChanged(mapSoundIdToPositionInInternalAdapter[sound.id]!!, sound)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupSoundsHolder {
        val binding: SectionRowBinding =
            SectionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupSoundsHolder(
            binding,
            isSelectable,
            isSoundChangeable,
            mapSoundCategoryToInternalAdapter
        )
    }

    override fun onBindViewHolder(holder: GroupSoundsHolder, position: Int) {
        holder.onBind(mapSoundCategoryToSounds[categories[position]], event)
    }

    override fun getItemCount(): Int {
        return mapSoundCategoryToSounds.size
    }
}

class GroupSoundsHolder(
    private val binding: SectionRowBinding,
    private val isSelectable: Boolean,
    private val isSoundChangeable: Boolean,
    private val mapSoundCategoryToInternalAdapter: MutableMap<SoundCategory, SoundAdapter>,
) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var adapter: SoundAdapter
    fun onBind(sounds: List<Sound>?, event: MutableLiveData<SoundsEvent>) {
        sounds?.let {
            val soundCategory = sounds[0].category
            binding.sectionNameTv.text = soundCategory.title
            binding.sectionRv.layoutManager = GridLayoutManager(binding.root.context, 3)
            adapter =
                SoundAdapter(
                    it.toMutableList(),
                    event,
                    isSelectable = isSelectable,
                    isSoundChangeable = isSoundChangeable,
                )
            binding.sectionRv.adapter = adapter

            mapSoundCategoryToInternalAdapter[soundCategory] = adapter
        }
    }
}

class SoundAdapter(
    val sounds: MutableList<Sound>,
    val event: MutableLiveData<SoundsEvent>,
    private val isSelectable: Boolean,
    private val isSoundChangeable: Boolean,
) :
    RecyclerView.Adapter<SoundsHolder>() {

    fun updateItems(sounds: List<Sound>) {
        this.sounds.clear()
        this.sounds.addAll(sounds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsHolder {
        val binding =
            ItemSoundsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SoundsHolder(binding)
    }

    override fun onBindViewHolder(holder: SoundsHolder, position: Int) {
        showLog("onBindViewHolder: $sounds", TAG)
        holder.onBind(
            sounds[position],
            event,
            isSelectable,
            isSoundChangeable
        )
    }

    override fun getItemCount(): Int {
        return sounds.size
    }
}

class SoundsHolder(private val binding: ItemSoundsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        sound: Sound,
        event: MutableLiveData<SoundsEvent>,
        isSelectable: Boolean,
        isSoundChangeable: Boolean
    ) {
        mapSoundIdToPositionInInternalAdapter[sound.id] = bindingAdapterPosition
        binding.mixItemTv.text = sound.title
        binding.seekBar.visibility = View.INVISIBLE

        val userHasPremiumStatus = PremiumPreferences.userHasPremiumStatus(binding.root.context)
        if (sound.isPremium && !userHasPremiumStatus) {
            binding.crownIv.visibility = View.VISIBLE
        } else {
            binding.crownIv.visibility = View.GONE
        }

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
            event.value = SoundsEvent.OnAdditionalSoundClick(sound)
        }
        binding.soundsItemIv.setImageResource(sound.icon)
    }
}

//data class SoundHolderData(val adapter: SoundAdapter, val position: Int)





