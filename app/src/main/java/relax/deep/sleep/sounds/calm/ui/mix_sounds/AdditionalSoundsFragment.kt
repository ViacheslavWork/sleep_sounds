package relax.deep.sleep.sounds.calm.ui.mix_sounds

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import relax.deep.sleep.sounds.calm.R
import relax.deep.sleep.sounds.calm.databinding.FragmentAdditionalSoundsBinding
import relax.deep.sleep.sounds.calm.model.Sound
import relax.deep.sleep.sounds.calm.service.PlayerService
import relax.deep.sleep.sounds.calm.ui.dialogs.UnlockForFreeDialog
import relax.deep.sleep.sounds.calm.ui.sounds.SoundsEvent
import relax.deep.sleep.sounds.calm.ui.sounds.SoundsFragment
import relax.deep.sleep.sounds.calm.utils.Constants
import relax.deep.sleep.sounds.calm.utils.MyLog.showLog
import relax.deep.sleep.sounds.calm.utils.PremiumPreferences


private const val TAG = "AdditionalSoundsFragment"

class AdditionalSoundsFragment : Fragment() {
    companion object {
        const val mixIdKey = "MIX_ID_KEY"
    }

    private var mixId: Long? = 0
    private val additionalSoundsViewModel: MixesSoundsViewModel by sharedViewModel()

    private lateinit var sectionRecyclerView: RecyclerView
    private var soundsAdapter: GroupSoundsAdapter? = null
    private val soundsEvent: MutableLiveData<SoundsEvent> = MutableLiveData()

    private lateinit var selectedSoundsRecyclerView: RecyclerView
    private lateinit var selectedSoundsAdapter: SelectedSoundsAdapter

    private var lastOnClick: SoundsEvent? = null

    private var _binding: FragmentAdditionalSoundsBinding? = null
    private val binding get() = _binding!!

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
            Log.d(TAG, "ServiceConnection: disconnected from service.")
            isServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mixId = arguments?.getLong(mixIdKey)
//        additionalSoundsViewModel.loadSounds()
        setFragmentResultListener(SoundsFragment.playPremiumSoundRequest) { _, bundle ->
            val isPlay: Boolean = bundle.getBoolean(SoundsFragment.playAfterVideoKey)
            // Do something with the result
            if (isPlay) {
                lastOnClick?.let { playStopSound(it) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_greeting_fragment, null)
        _binding = FragmentAdditionalSoundsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindService()

        setUpListeners()

        setUpSectionRecyclerView()
        setUpSectionAdapter()

        setUpSelectedSoundsRecyclerView()
        setUpSelectedSoundsAdapter()

        observeSounds()
        observeSelection()
        observeSelectedEvents()
        observeSectionSoundsEvents()
    }

    override fun onResume() {
        startAdAnimation()
        super.onResume()
    }

    override fun onPause() {
        binding.crownToolbarIv.clearAnimation()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        sendCommandToPlayerService(Constants.ACTION_STOP_SERVICE, null)
        if (isServiceBound) {
            unbindService()
        }
        super.onDestroy()
    }

    private fun setUpListeners() {
        binding.crownToolbarIv.setOnClickListener {
            findNavController().navigate(
                AdditionalSoundsFragmentDirections
                    .actionAdditionalSoundsFragmentToGoPremiumFragment()
            )
        }
        binding.applyBtn.setOnClickListener {
            mixId?.let { additionalSoundsViewModel.saveChangesInMix(mixId!!) }
            requireActivity().onBackPressed()
        }
        binding.closeBtn.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun startAdAnimation() {
        binding.crownToolbarIv.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.ad_animation
            )
        )
    }

    private fun setUpSectionRecyclerView() {
        sectionRecyclerView = binding.soundsRv
        sectionRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpSectionAdapter() {
        lifecycleScope.launch {
            soundsAdapter = GroupSoundsAdapter(
                sounds = additionalSoundsViewModel.loadSounds(),
                event = soundsEvent
            )
            soundsAdapter?.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            sectionRecyclerView.adapter = soundsAdapter
        }
    }

    private fun setUpSelectedSoundsRecyclerView() {
        selectedSoundsRecyclerView = binding.additionalSoundsRv
        selectedSoundsRecyclerView.layoutManager =
            LinearLayoutManager(context).apply { orientation = RecyclerView.HORIZONTAL }
    }

    private fun setUpSelectedSoundsAdapter() {
        selectedSoundsAdapter = SelectedSoundsAdapter()
        selectedSoundsAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        selectedSoundsRecyclerView.adapter = selectedSoundsAdapter
    }

    private fun observeSounds() {
        /*additionalSoundsViewModel.sections.observe(viewLifecycleOwner) {
            soundsAdapter.submitList(it.toMutableList())
        }*/
        additionalSoundsViewModel.sounds.observe(viewLifecycleOwner) {
            showLog("observeSounds: $it", TAG)
            soundsAdapter?.updateSounds(it)
            lastOnClick?.let { event ->
                soundsAdapter?.notifySoundChanged(event.sound)
            }
        }
    }

    private fun observeSelection() {
        additionalSoundsViewModel.selectedSounds.observe(viewLifecycleOwner) {
            showLog(it.toString())
            selectedSoundsAdapter.submitList(it.toMutableList())
            it.forEach { sound ->
                selectSoundInSections(sound)
            }
        }
    }

    private fun observeSelectedEvents() {
        selectedSoundsAdapter.event.observe(viewLifecycleOwner) {
            if (it is SoundsEvent.OnRemoveClick) {
                playStopSound(it)
                lastOnClick = it
            } else if (it is SoundsEvent.OnSeekBarChanged) {
                showLog(it.sound.volume.toString())
                playerService?.changeVolume(it.sound)
            }
            additionalSoundsViewModel.handleEvent(it)
        }
    }

    private fun observeSectionSoundsEvents() {
        soundsEvent.observe(viewLifecycleOwner) {
            if (it is SoundsEvent.OnAdditionalSoundClick) {
                showLog("observeSectionSoundsEvents: ${it.sound}", TAG)
                lastOnClick = it
                if (!it.sound.isPremium
                    || it.sound.isPlaying
                    || PremiumPreferences.userHasPremiumStatus(requireContext())
                ) {
                    playStopSound(it)
                } else {
                    findNavController().navigate(
                        R.id.action_mixesSoundsFragment_to_unlockForFreeFragment,
                        bundleOf(UnlockForFreeDialog.soundKey to it.sound)
                    )
                }
            }
        }
    }

    private fun playStopSound(soundsEvent: SoundsEvent) {
        soundsEvent.sound.apply {
            isPlaying = !isPlaying
            isPremium = false
        }
        sendCommandToPlayerService(Constants.ACTION_PLAY_OR_STOP_SOUND, soundsEvent.sound)
        additionalSoundsViewModel.handleEvent(soundsEvent)
//        selectSoundInSections(soundsEvent.sound.apply { isPlaying = !isPlaying })
    }

    private fun selectSoundInSections(sound: Sound) {
//        val soundHolderData = soundsAdapter.getMapSoundIdToHolder()[sound.id]
//        soundHolderData?.adapter?.notifyItemChanged(soundHolderData.position, sound)
    }

    //service
    private fun sendCommandToPlayerService(action: String, sound: Sound?) {
        Intent(requireContext(), PlayerService::class.java).also {
            it.putExtra(Constants.LAUNCHER, Constants.MIX_LAUNCHER)
            it.putExtra(Constants.EXTRA_SOUND, sound)
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
}