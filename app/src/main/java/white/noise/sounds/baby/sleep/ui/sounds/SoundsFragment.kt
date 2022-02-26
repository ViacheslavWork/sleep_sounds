package white.noise.sounds.baby.sleep.ui.sounds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import white.noise.sounds.baby.sleep.BuildConfig
import white.noise.sounds.baby.sleep.databinding.FragmentSoundsBinding

class SoundsFragment : Fragment() {
    private val TAG = "SoundsFragment"
    private val soundsViewModel: SoundsViewModel by sharedViewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SectionAdapter

    private var _binding: FragmentSoundsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        setUpRecyclerView()
        setUpAdapter()

        setUpListeners()

        observeSounds()
        observeSelectedSounds()
    }

    private fun observeSelectedSounds() {
        soundsViewModel.selectedSounds.observe(viewLifecycleOwner){
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
    }

    private fun observeSounds() {
        soundsViewModel.sounds.observe(viewLifecycleOwner) {
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
        adapter.event.observe(viewLifecycleOwner) {
            soundsViewModel.handleEvent(it)
        }
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}