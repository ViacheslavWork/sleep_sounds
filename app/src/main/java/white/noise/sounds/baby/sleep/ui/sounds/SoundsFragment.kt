package white.noise.sounds.baby.sleep.ui.sounds

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentSoundsBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.utils.Constants
import white.noise.sounds.baby.sleep.utils.Constants.EXTRA_SOUND
import white.noise.sounds.baby.sleep.utils.Constants.LAUNCHER
import white.noise.sounds.baby.sleep.utils.Constants.SOUNDS_LAUNCHER

class SoundsFragment : Fragment() {
    private val TAG = "SoundsFragment"
    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SectionAdapter
    private var mIsPause = false

    var playerService: PlayerService? = null

    // Boolean to check if our fragment is bound to service or not
    var isServiceBound: Boolean = false
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            Log.d(TAG, "ServiceConnection: connected to service.")
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            val binder = iBinder as PlayerService.MyBinder
            playerService = binder.service
            isServiceBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(TAG, "ServiceConnection: disconnected from service.")
            isServiceBound = false
        }
    }

    private var _binding: FragmentSoundsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.playerGroup.visibility = View.VISIBLE
        bindService()

        setUpRecyclerView()
        setUpAdapter()

        setUpListeners()
        setUpPlayPauseButton()

        observeSounds()
        observeSelectedSounds()
        observeRecyclerEvent()
        observeTimer()
    }

    override fun onResume() {
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
        soundsViewModel.selectedSounds.observe(viewLifecycleOwner) {
            when {
                PlayerService.launcher == SOUNDS_LAUNCHER -> {
                    when {
                        it.isNotEmpty() && mIsPause -> {
                            binding.playIb.isClickable = true
                            binding.playIb.isEnabled = true
                            binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                            binding.playIb.tag = R.drawable.ic_play_rounded
                        }
                        it.isNotEmpty() -> {
                            binding.playIb.isClickable = true
                            binding.playIb.isEnabled = true
                            binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                            binding.playIb.tag = R.drawable.ic_pause_rounded
                        }
                        else -> {
                            binding.playIb.isClickable = false
                            binding.playIb.isEnabled = false
                            binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                            binding.playIb.tag = R.drawable.ic_play_rounded
                        }
                    }
                }
                it.isNotEmpty() -> {
                    binding.playIb.isClickable = true
                    binding.playIb.isEnabled = true
                    binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                    binding.playIb.tag = R.drawable.ic_play_rounded
                }
                else -> {
                    binding.playIb.isClickable = false
                    binding.playIb.isEnabled = false
                    binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                    binding.playIb.tag = R.drawable.ic_play_rounded
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
                    binding.playIb.isClickable = true
                } else {
                    binding.playIb.isClickable = false
                    binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                    binding.playIb.tag = R.drawable.ic_pause_rounded
                }
            }
        }
    }

    private fun observeTimer() {
        PlayerService.isTimerRunning.observe(viewLifecycleOwner) {
            if (it) {
                PlayerService.timerTime.observe(viewLifecycleOwner) { timerTime ->
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
            binding.selectedIb.isEnabled = it.isNotEmpty()
            binding.selectedTv.isEnabled = it.isNotEmpty()
        }
    }

    private fun observeRecyclerEvent() {
        adapter.event.observe(viewLifecycleOwner) {
            soundsViewModel.handleEvent(it)
            if (it is SoundsEvent.OnSoundClick) {
                if (PlayerService.launcher != SOUNDS_LAUNCHER) {
                    sendCommandToPlayerService(Constants.ACTION_STOP_ALL_SOUNDS, it.sound)
                }
                if (it.sound.isPlaying) {
                    sendCommandToPlayerService(Constants.ACTION_PLAY_SOUND, it.sound)
                } else {
                    sendCommandToPlayerService(Constants.ACTION_STOP_SOUND, it.sound)
                }
            } else if (it is SoundsEvent.OnSeekBarChanged) {
                playerService?.changeVolume(it.sound)
            }
        }
    }

    //service
    private fun sendCommandToPlayerService(action: String, sound: Sound?) {
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(LAUNCHER, SOUNDS_LAUNCHER)
            it.putExtra(EXTRA_SOUND, sound)
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