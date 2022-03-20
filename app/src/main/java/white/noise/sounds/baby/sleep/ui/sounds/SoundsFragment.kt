package white.noise.sounds.baby.sleep.ui.sounds

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.blurry.Blurry
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentSoundsBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.service.TimerService
import white.noise.sounds.baby.sleep.ui.dialogs.UnlockForFreeDialog
import white.noise.sounds.baby.sleep.utils.Constants
import white.noise.sounds.baby.sleep.utils.Constants.EXTRA_MIX_ID
import white.noise.sounds.baby.sleep.utils.Constants.EXTRA_SOUND
import white.noise.sounds.baby.sleep.utils.Constants.LAUNCHER
import white.noise.sounds.baby.sleep.utils.Constants.SOUNDS_LAUNCHER

class SoundsFragment : Fragment() {
    companion object {
        const val UNLOCK_FOR_FREE_DIALOG_REQUEST_CODE = 1
        const val PLAY_PREMIUM_SOUND_AFTER_VIDEO = "PLAY_PREMIUM_SOUND_AFTER_VIDEO"
        const val playAfterVideoKey = "PLAY_AFTER_VIDEO_KEY"
        const val playPremiumSoundRequest = "PLAY_PREMIUM_SOUND_REQUEST"
    }

    private val TAG = "SoundsFragmentTAG"
    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SectionAdapter
    private var mIsPause = false
    private lateinit var lastOnClick: SoundsEvent.OnSoundClick

    var playerService: PlayerService? = null

    // Boolean to check if our fragment is bound to service or not
    var isServiceBound: Boolean = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            val binder = iBinder as PlayerService.MyBinder
            playerService = binder.service
            isServiceBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isServiceBound = false
        }
    }
    private var _binding: FragmentSoundsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(playPremiumSoundRequest) { _, bundle ->
            val isPlay: Boolean = bundle.getBoolean(playAfterVideoKey)
            // Do something with the result
            if (isPlay) {
                playStopSoundProgrammatically()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.background, null)

        _binding = FragmentSoundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        soundsViewModel.updateSections()
        startAdAnimation()
        super.onResume()
    }

    override fun onPause() {
        binding.crownSoundsToolbarIv.clearAnimation()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        if (isServiceBound) {
            unbindService()
        }
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpPlayerVisibility(isVisible = true)
        setUpPlayerVisibility(isVisible = false)
        setUpRecyclerView()
        setUpAdapter()

        setUpListeners()
        setUpPlayPauseButton()

        observeSounds()
        observeSelectedSounds()
        observeRecyclerEvent()
        observeTimer()
        observePlayable()
        observePause()
    }



    private fun setUpPlayerVisibility(isVisible: Boolean) {
        if (isVisible) binding.playerGroup.visibility = View.VISIBLE
        else binding.playerGroup.visibility = View.GONE
    }

    private fun observePlayable() {
        PlayerService.isPlayable.observe(viewLifecycleOwner) {
            if (it) {
                bindService()
            }
            if (it && PlayerService.launcher == Constants.SOUNDS_LAUNCHER) {
                setUpPlayerVisibility(isVisible = true)
            }
        }
    }

    private fun setUpListeners() {
        binding.crownSoundsToolbarIv.setOnClickListener {
            findNavController().navigate(
                SoundsFragmentDirections.actionNavigationSoundsToGoPremiumFragment()
            )
        }
        binding.timerIb.setOnClickListener {
            findNavController().navigate(SoundsFragmentDirections.actionNavigationSoundsToSetTimerFragment())
        }
        binding.selectedIb.setOnClickListener {
            findNavController().navigate(
                SoundsFragmentDirections.actionNavigationSoundsToCustomMixDialog()
            )
        }
        binding.playIb.setOnClickListener {
            if (PlayerService.launcher == SOUNDS_LAUNCHER) {
                sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
            } else {
                sendCommandToPlayerService(Constants.ACTION_STOP_ALL_SOUNDS, null)
                soundsViewModel.selectedSounds.value?.forEach {
                    sendCommandToPlayerService(Constants.ACTION_PLAY_SOUND, it)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        recyclerView = binding.soundsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpAdapter() {
        adapter = SectionAdapter()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        recyclerView.adapter = adapter
    }

    private fun setUpPlayPauseButton() {
        PlayerService.isPause.observe(viewLifecycleOwner) { isPause ->
            if (PlayerService.launcher == SOUNDS_LAUNCHER) {
                if (isPause) {
                    mIsPause = true
                    binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                    binding.playIb.tag = R.drawable.ic_play_rounded
                } else {
                    mIsPause = false
                    binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                    binding.playIb.tag = R.drawable.ic_pause_rounded
                }
            }
        }
        PlayerService.isPlayable.observe(viewLifecycleOwner) { isPlayable ->
            if (PlayerService.launcher == SOUNDS_LAUNCHER) {
                if (isPlayable) {
                    binding.playIb.isClickable = true
                } else {
                    binding.playIb.isClickable = false
                    binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                    binding.playIb.tag = R.drawable.ic_pause_rounded
                }
            }
        }
    }

    private fun startAdAnimation() {
        binding.crownSoundsToolbarIv.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.ad_animation
            )
        )
    }

    //observe
    private fun observeSounds() {
        soundsViewModel.sections.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }
    }

    private fun observePause() {
        PlayerService.isPause.observe(viewLifecycleOwner) { isPause ->
            if (PlayerService.launcher == SOUNDS_LAUNCHER) {
                if (isPause) {
                    mIsPause = true
                    binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                    binding.playIb.tag = R.drawable.ic_play_rounded
                } else {
                    mIsPause = false
                    binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                    binding.playIb.tag = R.drawable.ic_pause_rounded
                }
            }
        }
        PlayerService.isPlayable.observe(viewLifecycleOwner) { isPlayable ->
            if (PlayerService.launcher == SOUNDS_LAUNCHER) {
                if (isPlayable) {
//                    binding.playIb.isClickable = true
                } else {
//                    binding.playIb.isClickable = false
                    binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                    binding.playIb.tag = R.drawable.ic_pause_rounded
                }
            }
        }
    }

    private fun observeTimer() {
        TimerService.isTimerStarted.observe(viewLifecycleOwner) {
            if (it) {
                TimerService.timerTime.observe(viewLifecycleOwner) { timerTime ->
                    binding.timerTv.text = timerTime.toString()
                }
            } else {
                binding.timerTv.text = getString(R.string.timer)
            }
        }
    }

    private fun observeSelectedSounds() {
        soundsViewModel.selectedSounds.observe(viewLifecycleOwner) {
            binding.numSoundsTv.text = it.size.toString()
//            binding.selectedIb.isEnabled = it.isNotEmpty()
//            binding.selectedTv.isEnabled = it.isNotEmpty()
            when {
                PlayerService.launcher == SOUNDS_LAUNCHER -> {
                    when {
                        it.isNotEmpty() && mIsPause -> {
//                            binding.playIb.isClickable = true
//                            binding.playIb.isEnabled = true
                            binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                            binding.playIb.tag = R.drawable.ic_play_rounded
                        }
                        it.isNotEmpty() -> {
//                            binding.playIb.isClickable = true
//                            binding.playIb.isEnabled = true
                            binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                            binding.playIb.tag = R.drawable.ic_pause_rounded
                        }
                        else -> {
//                            binding.playIb.isClickable = false
//                            binding.playIb.isEnabled = false
                            binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                            binding.playIb.tag = R.drawable.ic_play_rounded
                            setUpPlayerVisibility(isVisible = false)
                        }
                    }
                }
                it.isNotEmpty() -> {
//                    binding.playIb.isClickable = true
//                    binding.playIb.isEnabled = true
                    binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                    binding.playIb.tag = R.drawable.ic_play_rounded
                }
                else -> {
//                    binding.playIb.isClickable = false
//                    binding.playIb.isEnabled = false
                    binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                    binding.playIb.tag = R.drawable.ic_play_rounded
                }
            }
        }
    }

    private fun observeRecyclerEvent() {
        adapter.event.observe(viewLifecycleOwner) {
            if (it is SoundsEvent.OnSoundClick) {
                lastOnClick = it
                if (!it.sound.isPremium || it.sound.isPlaying) {
                    playStopSoundProgrammatically()
                } else {
                    findNavController().navigate(
                        R.id.action_navigation_sounds_to_unlockForFreeFragment,
                        bundleOf(UnlockForFreeDialog.soundKey to it.sound)
                    )
                }
            } else if (it is SoundsEvent.OnSeekBarChanged) {
                playerService?.changeVolume(it.sound)
                soundsViewModel.handleEvent(it)
            }
        }
    }

    private fun playStopSoundProgrammatically() {
        lastOnClick.soundsHolder.bindingAdapter?.notifyItemChanged(
            lastOnClick.soundsHolder.bindingAdapterPosition,
            lastOnClick.sound.apply { isPlaying = !isPlaying }
        )
        playPauseSound(lastOnClick.sound)
        soundsViewModel.handleEvent(lastOnClick)
    }

    private fun playPauseSound(sound: Sound) {
        val launcher = PlayerService.launcher
        Log.i(TAG, "launcher: $launcher")
        if (PlayerService.launcher != SOUNDS_LAUNCHER) {
            sendCommandToPlayerService(Constants.ACTION_STOP_ALL_SOUNDS, null)
            if (PlayerService.isPause.value == true) {
                sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
            }
        }
        if (sound.isPlaying) {
            sendCommandToPlayerService(Constants.ACTION_PLAY_SOUND, sound)
            if (PlayerService.isPause.value == true) {
                sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
            }
        } else {
            sendCommandToPlayerService(Constants.ACTION_STOP_SOUND, sound)
        }
    }

    //service
    private fun sendCommandToPlayerService(action: String, sound: Sound?) {
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(LAUNCHER, SOUNDS_LAUNCHER)
            it.putExtra(EXTRA_SOUND, sound)
            it.putExtra(EXTRA_MIX_ID, Constants.NO_MIX_ID)
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun bindService() {
        Intent(requireContext(), PlayerService::class.java).also { intent ->
            requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {
        Intent(requireContext(), PlayerService::class.java).also {
            requireActivity().unbindService(serviceConnection)
        }
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}