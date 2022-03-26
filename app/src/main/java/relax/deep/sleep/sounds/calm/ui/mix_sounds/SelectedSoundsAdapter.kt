package relax.deep.sleep.sounds.calm.ui.mix_sounds

import android.content.Context
import android.util.DisplayMetrics
import android.view.*
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.ItemSoundsBinding
import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.ui.sounds.SoundsEvent

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
        //this for handling touch seek bar in horizontal orientation
        binding.seekBar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->                    // Disallow ScrollView to intercept touch events.
                    v.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP ->                     //Allow ScrollView to intercept touch events once again.
                    v.parent.requestDisallowInterceptTouchEvent(true)
            }
            // Handle RecyclerView touch events.
            v.onTouchEvent(event)
            true
        }
        binding.root.setOnClickListener {
//            event.value = SoundsEvent.OnSoundClick(sound,bindingAdapterPosition)
        }
        binding.removeIv.setOnClickListener {
            sound.isPlaying = false
            event.value =
                SoundsEvent.AdditionalSoundsEvent.OnRemoveClick(sound, bindingAdapterPosition)
        }

        binding.root.background = ResourcesCompat.getDrawable(
            binding.root.resources,
            R.drawable.gradient_liner_bg3_rounded_corners,
            null
        )

        binding.soundsItemIv.setImageResource(sound.icon)

        val windowManager =
            binding.root.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val density = displayMetrics.density
        val width = (((displayMetrics.widthPixels / density) / 3 - 21) * density).toInt()
        val layoutParams = binding.root.layoutParams
        layoutParams.width = width
        binding.root.layoutParams = layoutParams
    }
}

private class SoundDiffCallback : DiffUtil.ItemCallback<Sound>() {
    override fun areItemsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        (oldItem.title == newItem.title)

    override fun areContentsTheSame(oldItem: Sound, newItem: Sound): Boolean =
        (oldItem == newItem)
}