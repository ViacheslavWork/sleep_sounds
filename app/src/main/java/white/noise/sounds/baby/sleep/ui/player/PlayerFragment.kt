package white.noise.sounds.baby.sleep.ui.player

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
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.ButtonPlayerBinding
import white.noise.sounds.baby.sleep.databinding.FragmentPlayerBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.service.TimerService
import white.noise.sounds.baby.sleep.ui.mix_sounds.AdditionalSoundsFragment
import white.noise.sounds.baby.sleep.utils.Constants

private const val TAG = "PlayerFragment"

class PlayerFragment : Fragment() {
    private val playerViewModel: PlayerViewModel by sharedViewModel()
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var mixId: Long? = 0
    private var isStartPlaying = true

    companion object {
        const val mixIdKey = "MIX_ID_KEY"
        const val isStartPlayingArg = "IS_START_PLAYING_ARG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mixId = arguments?.getLong(mixIdKey)
        isStartPlaying = arguments?.getBoolean(isStartPlayingArg, false) == true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_player, null)
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Variable for storing instance of our service class
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindService()
        observeService()

        setUpEditBtn()
        setUpListeners()

        observeSounds()
        observeMix()
        observeTimer()
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

    private fun playStopMix(sounds: List<Sound>) {
        sendCommandToPlayerService(Constants.ACTION_STOP_ALL_SOUNDS, null)
        if (PlayerService.isPause.value == true) {
            sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
        }
        sounds.forEach {
            sendCommandToPlayerService(Constants.ACTION_PLAY_OR_STOP_SOUND, it)
        }
    }

    private fun playPauseMix() {
        sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
    }

    private fun observeSounds() {
        mixId?.let {
            playerViewModel.loadSounds(it).observe(viewLifecycleOwner) { sounds ->
                if (isStartPlaying) {
                    isStartPlaying = false
                    playStopMix(sounds)
                }
                initButtons(sounds)
            }
        }
    }

    private fun observeMix() {
        mixId?.let {
            playerViewModel.getMix(it).observe(viewLifecycleOwner) { mix ->
                binding.titleTv.text = mix.title
            }
        }
    }

    private fun observeService() {
        PlayerService.isPause.observe(viewLifecycleOwner) {
            binding.playerPlayPauseBtn.setImageDrawable(
                if (it) {
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_icn_play, null)
                } else {
                    ResourcesCompat.getDrawable(resources, R.drawable.icn_pause, null)
                }
            )
        }
    }

    private fun observeTimer() {
        TimerService.isTimerStarted.observe(viewLifecycleOwner) {
            if (it) {
                binding.timeTv.visibility = View.VISIBLE
                TimerService.timerTime.observe(viewLifecycleOwner) { timerTime ->
                    binding.timeTv.text = timerTime.toString()
                }
            } else {
                binding.timeTv.visibility = View.GONE
            }
        }
    }

    private fun setUpListeners() {
        binding.downArrowToolbarIv.setOnClickListener { requireActivity().onBackPressed() }
        binding.playerPlayPauseBtn.setOnClickListener { playPauseMix() }
        binding.timerTv.setOnClickListener { findNavController().navigate(PlayerFragmentDirections.actionPlayerFragmentToSetTimerFragment()) }
    }

    private fun setUpEditBtn() {
        binding.editBtn.root.visibility = View.VISIBLE
        binding.editBtn.smallRoundIv.visibility = View.VISIBLE
        binding.editBtn.numSoundsTv.visibility = View.VISIBLE
        binding.editBtn.playerBtn.setOnClickListener {
            navigateToAdditionalSoundsFragment()
        }
    }

    private fun initButtons(it: List<Sound>) {
        getButtons().forEach { it.root.visibility = View.GONE }
        binding.editBtn.numSoundsTv.text = it.size.toString()
        it.take(5).forEachIndexed { index, sound ->
            getButtons()[index].apply {
                playerBtn.setOnClickListener {
                    navigateToAdditionalSoundsFragment()
                }
                root.visibility = View.VISIBLE
                btnTv.text =
                    String.format(resources.getString(R.string.volume_percentage), sound.volume)
                playerBtn.setImageResource(sound.icon)
            }
        }
    }

    private fun getButtons(): List<ButtonPlayerBinding> {
        return mutableListOf<ButtonPlayerBinding>()
            .apply {
                add(binding.btn1)
                add(binding.btn2)
                add(binding.btn3)
                add(binding.btn4)
                add(binding.btn5)
            }.toList()
    }

    private fun navigateToAdditionalSoundsFragment() {
        isStartPlaying = true
        findNavController().navigate(
            R.id.action_playerFragment_to_additionalSoundsFragment,
            bundleOf(AdditionalSoundsFragment.mixIdKey to mixId)
        )
    }

    //service
    private fun sendCommandToPlayerService(action: String, sound: Sound?) {
        Log.i(TAG, "sendCommandToPlayerService: ")
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(Constants.LAUNCHER, Constants.MIX_LAUNCHER)
            it.putExtra(Constants.EXTRA_SOUND, sound)
            it.putExtra(Constants.EXTRA_MIX_ID, mixId)
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

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}