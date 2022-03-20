package white.noise.sounds.baby.sleep.ui.mix_sounds

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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.R
import white.noise.sounds.baby.sleep.databinding.FragmentAdditionalSoundsBinding
import white.noise.sounds.baby.sleep.model.Sound
import white.noise.sounds.baby.sleep.service.PlayerService
import white.noise.sounds.baby.sleep.ui.sounds.SectionAdapter
import white.noise.sounds.baby.sleep.ui.sounds.SoundsEvent
import white.noise.sounds.baby.sleep.utils.Constants
import white.noise.sounds.baby.sleep.utils.MyLog.showLog


private const val TAG = "AdditionalSoundsFragment"

class AdditionalSoundsFragment : Fragment() {
    companion object {
        const val mixIdKey = "MIX_ID_KEY"
    }

    private var mixId: Long? = 0
    private val additionalSoundsViewModel: MixesSoundsViewModel by sharedViewModel()

    private lateinit var sectionRecyclerView: RecyclerView
    private lateinit var sectionAdapter: SectionAdapter

    private lateinit var selectedSoundsRecyclerView: RecyclerView
    private lateinit var selectedSoundsAdapter: SelectedSoundsAdapter

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
        mixId?.let { additionalSoundsViewModel.loadMixSounds(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        requireActivity().findViewById<ConstraintLayout>(R.id.container).background =
            ResourcesCompat.getDrawable(resources, R.drawable.background, null)
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
        sectionAdapter = SectionAdapter(isSelectable = true, isSoundChangeable = false)
        sectionAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        sectionRecyclerView.adapter = sectionAdapter
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
        additionalSoundsViewModel.sections.observe(viewLifecycleOwner) {
            sectionAdapter.submitList(it.toMutableList())
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
            if (it is SoundsEvent.AdditionalSoundsEvent.OnRemoveClick) {
                sendCommandToPlayerService(Constants.ACTION_PLAY_OR_STOP_SOUND, it.sound)
                additionalSoundsViewModel.handleEvent(it)
                selectSoundInSections(it.sound.apply { isPlaying = false })
            } else if (it is SoundsEvent.OnSeekBarChanged) {
                showLog(it.sound.volume.toString())
                playerService?.changeVolume(it.sound)
            }
            additionalSoundsViewModel.handleEvent(it)
        }
    }

    private fun observeSectionSoundsEvents() {
        sectionAdapter.event.observe(viewLifecycleOwner) {
            if (it is SoundsEvent.OnSoundClick) {
                if (!it.sound.isPremium || it.sound.isPlaying) {
                    selectSoundInSections(it.sound.apply { isPlaying = !isPlaying })
                    sendCommandToPlayerService(Constants.ACTION_PLAY_OR_STOP_SOUND, it.sound)
                    additionalSoundsViewModel.handleEvent(it)
                } else {
                    //TODO
                }
            }
        }
    }

    private fun selectSoundInSections(sound: Sound) {
        val soundHolderData = sectionAdapter.getMapSoundIdToHolder()[sound.id]
        soundHolderData?.adapter?.notifyItemChanged(soundHolderData.position, sound)
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

    /*private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }*/
}