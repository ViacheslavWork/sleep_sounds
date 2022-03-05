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

class SoundsFragment : Fragment() {
    private val TAG = "SoundsFragment"
    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SectionAdapter

    // Variable for storing instance of our service class
    var playerService: PlayerService? = null

    // Boolean to check if our fragment is bound to service or not
    var isServiceBound: Boolean = false

    private var _binding: FragmentSoundsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        observeSounds()
        observeSelectedSounds()
        observeRecyclerEvent()
        observeTimer()
        observePlayer()
    }

    private fun observePlayer() {
        PlayerService.isPause.observe(viewLifecycleOwner) { isPause ->
            if (isPause) {
                binding.playIb.setImageResource(R.drawable.ic_play_rounded)
                binding.playIb.tag = R.drawable.ic_play_rounded
            } else {
                binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                binding.playIb.tag = R.drawable.ic_pause_rounded
            }
        }
        PlayerService.isPlayable.observe(viewLifecycleOwner) { isPlayable ->
            if (isPlayable) {
                binding.playIb.isClickable = true
            } else {
                binding.playIb.isClickable = false
                binding.playIb.setImageResource(R.drawable.ic_pause_rounded)
                binding.playIb.tag = R.drawable.ic_pause_rounded
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
            sendCommandToPlayerService(Constants.ACTION_PLAY_OR_PAUSE_ALL_SOUNDS, null)
        }
    }


    private fun observeSounds() {
        soundsViewModel.sections.observe(viewLifecycleOwner) {
            it.forEach { sound -> showLog(sound.toString()) }
            adapter.submitList(it.toMutableList())
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

    private fun observeRecyclerEvent() {
        adapter.event.observe(viewLifecycleOwner) {
            soundsViewModel.handleEvent(it)
            if (it is SoundsEvent.OnSoundClick) {
                sendCommandToPlayerService(Constants.ACTION_PLAY_OR_STOP_SOUND, it.sound)
            } else if (it is SoundsEvent.OnSeekBarChanged) {
                playerService?.changeVolume(it.sound)
            }
        }
    }

    private fun sendCommandToPlayerService(action: String, sound: Sound?) {
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(Constants.EXTRA_SOUND, sound)
            it.action = action
            requireContext().startService(it)
        }
    }

    /**
     * Used to bind to our service class
     */
    private fun bindService() {
        Intent(requireContext(), PlayerService::class.java).also { intent ->
            requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Used to unbind and stop our service class
     */
    private fun unbindService() {
        Intent(requireContext(), PlayerService::class.java).also {
            requireActivity().unbindService(serviceConnection)
        }
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

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}